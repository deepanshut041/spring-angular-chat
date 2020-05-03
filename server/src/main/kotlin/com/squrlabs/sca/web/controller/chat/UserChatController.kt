package com.squrlabs.sca.web.controller.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.service.chat.UserChatService
import com.squrlabs.sca.domain.service.user.UserService
import com.squrlabs.sca.util.ApiResponse
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.DateTimeUtil
import com.squrlabs.sca.util.auth.util.UserPrincipal
import com.squrlabs.sca.web.controller.chat.UserChatController.Companion.USER_CHAT_BASE_URI
import com.squrlabs.sca.web.dto.chat.FriendProfileDto
import com.squrlabs.sca.web.dto.chat.MessageDto
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@RestController
@RequestMapping(USER_CHAT_BASE_URI)
@Tag(name = "User Chat", description = "This contains url related user Conversations")
class UserChatController(
        @Autowired val userService: UserService,
        @Autowired val userChatService: UserChatService
) {

    @GetMapping(consumes = ["application/json"])
    fun getFriends(@RequestParam("from", required = false) from: String?): ResponseEntity<List<FriendProfileDto>> {
        val user = getCurrentUser()
        val updatedAfter = DateTimeUtil.getDateFromString(from)

        val friends = userChatService.getConversations(user.id, updatedAfter)

        return ResponseEntity.ok(friends.map { FriendProfileDto(it.id, it.email, it.name, it.imgUrl, it.isBlocked, it.blockedBy) })
    }

    @GetMapping("/new", consumes = ["application/json"])
    fun startConversation(@RequestParam("email", required = true) email: String?): ResponseEntity<ApiResponse>{
        val user = getCurrentUser()
        email?.let {
            userChatService.newConversation(user.id, it)
        }?: run{
            throw BadRequestException("Sorry no email provides")
        }
        return  ResponseEntity.ok(ApiResponse(true, "Added user to Conversations"))
    }

    @GetMapping("/{id}/block", consumes = ["application/json"])
    fun blockUser(@PathVariable("id") id: String): ResponseEntity<ApiResponse>{
        val user = getCurrentUser()
        userChatService.blockConversation(id, user.id)
        return  ResponseEntity.ok(ApiResponse(true, "Successfully blocked user"))
    }

    @GetMapping("/{id}/unblock", consumes = ["application/json"])
    fun unblockUser(@PathVariable("id") id: String): ResponseEntity<ApiResponse>{
        val user = getCurrentUser()
        userChatService.unblockConversation(id, user.id)
        return  ResponseEntity.ok(ApiResponse(true, "Successfully unblocked user"))
    }

    @GetMapping("/{id}/messages", consumes = ["application/json"])
    fun getMessages(
            @PathVariable("id") id: String,
            @RequestParam("from", required = false) from: String?
    ): ResponseEntity<List<MessageDto>>{
        val user = getCurrentUser()
        val updatedAfter = DateTimeUtil.getDateFromString(from)
        val messages = userChatService.getMessages(id, user.id, updatedAfter)

        return  ResponseEntity.ok(messages.map {
            MessageDto(id, it.senderId, it.conversationId, it.content, it.mediaUrl, it.contentType, it.createdAt, it.updatedAt, it.receivedAt, it.readAt)
        })
    }

    @PostMapping("/{id}/messages")
    fun createMessage(
            @PathVariable("id") id: String,
            @RequestParam("file", required = false) file: MultipartFile?,
            @RequestParam("content", required = false) content: String = "",
            @RequestParam("content-type", required = true) contentStr: String
    ): ResponseEntity<ApiResponse>{
        val user = getCurrentUser()
        val contentType = ContentType.values().firstOrNull { it.name == contentStr.toUpperCase() }
        contentType?.let {
            file?.let {
                val filename = Path.of("chats", id, file.originalFilename)
                try{
                    Files.copy(file.inputStream, filename, StandardCopyOption.REPLACE_EXISTING)
                    userChatService.createMessage(id, user.id, content, filename.toString(), contentType)
                    return ResponseEntity.ok(ApiResponse(true, "Created Message Successfully"))
                } catch (e: IOException){
                    throw BadRequestException("Invalid file")
                }
            }?: run {
                userChatService.createMessage(id, user.id, content, "", ContentType.TEXT)
                return ResponseEntity.ok(ApiResponse(true, "Created Message Successfully"))
            }
        }?: run {
            throw BadRequestException("Content Type invalid")
        }

    }

    @GetMapping("/{conv_id}/messages/{msg_id}/read",consumes = ["application/json"])
    fun readMessage(
            @PathVariable("conv_id") convId: String,
            @PathVariable("msg_id") msgId: String
    ): ResponseEntity<ApiResponse>{
        val user = getCurrentUser()
        userChatService.updateMessages(msgId, convId, user.id, false)
        return  ResponseEntity.ok(ApiResponse(true, "Read Message Successfully"))
    }

    @GetMapping("/{conv_id}/messages/{msg_id}/received", consumes = ["application/json"])
    fun receivedMessage(
            @PathVariable("conv_id") convId: String,
            @PathVariable("msg_id") msgId: String
    ): ResponseEntity<ApiResponse>{
        val user = getCurrentUser()
        userChatService.updateMessages(msgId, convId, user.id, true)
        return  ResponseEntity.ok(ApiResponse(true, "Received Message Successfully"))
    }

    fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }

    companion object{
        const val USER_CHAT_BASE_URI = "/api/users/chat"
    }
}