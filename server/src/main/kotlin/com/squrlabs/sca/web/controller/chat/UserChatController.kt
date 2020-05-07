package com.squrlabs.sca.web.controller.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.service.chat.UserConversationService
import com.squrlabs.sca.domain.service.chat.UserMessageService
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.DateTimeUtil
import com.squrlabs.sca.util.auth.util.UserPrincipal
import com.squrlabs.sca.web.controller.chat.UserChatController.Companion.USER_CHAT_BASE_URI
import com.squrlabs.sca.web.dto.chat.FriendProfileResponse
import com.squrlabs.sca.web.dto.chat.MessageResponse
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
        @Autowired val userMessageService: UserMessageService,
        @Autowired val userConversationService: UserConversationService
) {

    @GetMapping
    fun getFriends(@RequestParam("from", required = false) from: String?): ResponseEntity<List<FriendProfileResponse>> {
        val user = getCurrentUser()
        val updatedAfter = DateTimeUtil.getDateFromString(from)
        val friends = userConversationService.getConversations(user.id, updatedAfter)
        return ResponseEntity.ok(friends.map { FriendProfileResponse(it.id, it.email, it.name, it.imgUrl, it.isBlocked, it.blockedBy) })
    }

    @GetMapping("/new")
    fun startConversation(@RequestParam("email", required = true) email: String?): ResponseEntity<FriendProfileResponse>{
        val user = getCurrentUser()
        email?.let {
            val friend = userConversationService.newConversation(user, it)
            return  ResponseEntity.ok( FriendProfileResponse(friend.id, friend.email, friend.name, friend.imgUrl, friend.isBlocked, friend.blockedBy))
        }?: run{
            throw BadRequestException("Sorry no email provides")
        }
    }

    @PostMapping("/messages")
    fun getAllMessages(
            @RequestBody ids: List<String>,
            @RequestParam("from", required = false) from: String?
    ): ResponseEntity<List<MessageResponse>> {
        val user = getCurrentUser()
        val messages = userMessageService.getAllMessages(ids, user.id)

        return  ResponseEntity.ok(messages.map {
            MessageResponse(it.id, it.senderId, it.conversationId, it.content, it.mediaUrl, it.contentType, it.createdAt, it.updatedAt, it.receivedAt, it.readAt)
        })
    }

    @GetMapping("/{cid}/block")
    fun blockUser(@PathVariable("cid") id: String): ResponseEntity<FriendProfileResponse>{
        val user = getCurrentUser()
        val friend = userConversationService.blockConversation(id, user)
        return ResponseEntity.ok( FriendProfileResponse(friend.id, friend.email, friend.name, friend.imgUrl, friend.isBlocked, friend.blockedBy))
    }

    @GetMapping("/{cid}/unblock")
    fun unblockUser(@PathVariable("cid") id: String): ResponseEntity<FriendProfileResponse>{
        val user = getCurrentUser()
        val friend = userConversationService.unblockConversation(id, user)
        return ResponseEntity.ok( FriendProfileResponse(friend.id, friend.email, friend.name, friend.imgUrl, friend.isBlocked, friend.blockedBy))
    }

    @GetMapping("/{cid}/messages")
    fun getMessages(
            @PathVariable("cid") id: String,
            @RequestParam("from", required = false) from: String?
    ): ResponseEntity<List<MessageResponse>>{
        val user = getCurrentUser()
        val updatedAfter = DateTimeUtil.getDateFromString(from)
        val messages = userMessageService.getMessages(id, user.id, updatedAfter)

        return  ResponseEntity.ok(messages.map {
            MessageResponse(it.id, it.senderId, it.conversationId, it.content, it.mediaUrl, it.contentType, it.createdAt, it.updatedAt, it.receivedAt, it.readAt)
        })
    }

    @PostMapping("/{cid}/messages/text")
    fun createMessageText(
            @PathVariable("cid") id: String,
            @RequestParam("content", required = false) content: String = ""
    ): ResponseEntity<MessageResponse>{
        val user = getCurrentUser()
        val msg = userMessageService.createMessage(id, user.id, content, "", ContentType.TEXT)
        return ResponseEntity.ok(MessageResponse(msg.id, msg.senderId, msg.conversationId, msg.content, msg.mediaUrl, msg.contentType,
                msg.createdAt, msg.updatedAt, msg.receivedAt, msg.readAt))
    }

    @PostMapping("/{cid}/messages/file")
    fun createMessageFile(
            @PathVariable("cid") id: String,
            @RequestParam("file", required = true) file: MultipartFile,
            @RequestParam("content", required = false) content: String = ""
    ): ResponseEntity<MessageResponse>{
        val user = getCurrentUser()
        val filename = Path.of("chats", id, file.originalFilename)
        try{
            Files.copy(file.inputStream, filename, StandardCopyOption.REPLACE_EXISTING)
            val msg = userMessageService.createMessage(id, user.id, content, filename.toString(), ContentType.FILE)
            return ResponseEntity.ok(MessageResponse(msg.id, msg.senderId, msg.conversationId, msg.content, msg.mediaUrl, msg.contentType,
                    msg.createdAt, msg.updatedAt, msg.receivedAt, msg.readAt))
        } catch (e: IOException){
            throw BadRequestException("Invalid file")
        }

    }

    @GetMapping("/{cid}/messages/{mid}/read",consumes = ["application/json"])
    fun readMessage(
            @PathVariable("cid") convId: String,
            @PathVariable("mid") msgId: String
    ): ResponseEntity<MessageResponse>{
        val user = getCurrentUser()
        val msg = userMessageService.updateMessages(msgId, convId, user.id, false)
        return ResponseEntity.ok(MessageResponse(msg.id, msg.senderId, msg.conversationId, msg.content, msg.mediaUrl, msg.contentType,
                msg.createdAt, msg.updatedAt, msg.receivedAt, msg.readAt))
    }

    @GetMapping("/{cid}/messages/{mid}/received", consumes = ["application/json"])
    fun receivedMessage(
            @PathVariable("cid") convId: String,
            @PathVariable("mid") msgId: String
    ): ResponseEntity<MessageResponse>{
        val user = getCurrentUser()
        val msg = userMessageService.updateMessages(msgId, convId, user.id, true)
        return ResponseEntity.ok(MessageResponse(msg.id, msg.senderId, msg.conversationId, msg.content, msg.mediaUrl, msg.contentType,
                msg.createdAt, msg.updatedAt, msg.receivedAt, msg.readAt))
    }

    fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }

    companion object{
        const val USER_CHAT_BASE_URI = "/api/users/chat"
    }
}