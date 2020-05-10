package com.squrlabs.sca.web.controller.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.FileModel
import com.squrlabs.sca.domain.service.chat.ChatService
import com.squrlabs.sca.domain.service.chat.MessageService
import com.squrlabs.sca.domain.service.file.FileStorageService
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.DateTimeUtil
import com.squrlabs.sca.util.auth.util.UserPrincipal
import com.squrlabs.sca.web.controller.chat.ChatController.Companion.USER_CHAT_BASE_URI
import com.squrlabs.sca.web.dto.chat.FriendProfileResponse
import com.squrlabs.sca.web.dto.chat.MessageResponse
import com.squrlabs.sca.web.dto.chat.MessageResponseMapper
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
class ChatController(
        @Autowired val messageService: MessageService,
        @Autowired val chatService: ChatService,
        @Autowired val fileStorageService: FileStorageService
) {

    @GetMapping
    fun getFriends(@RequestParam("from", required = false) from: String?): ResponseEntity<List<FriendProfileResponse>> {
        val user = getCurrentUser()
        val updatedAfter = DateTimeUtil.getDateFromString(from)
        val friends = chatService.getConversations(user.id, updatedAfter)
        return ResponseEntity.ok(friends.map { FriendProfileResponse(it.id, it.email, it.name, it.imgUrl, it.blockedBy) })
    }

    @PostMapping
    fun startConversation(@RequestParam("email", required = true) email: String?): ResponseEntity<FriendProfileResponse>{
        val user = getCurrentUser()
        email?.let {
            val friend = chatService.newConversation(user, it)
            return  ResponseEntity.ok( FriendProfileResponse(friend.id, friend.email, friend.name, friend.imgUrl, friend.blockedBy))
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
        val updatedAfter = DateTimeUtil.getDateFromString(from)
        val messages = messageService.getAllMessages(ids, user.id, updatedAfter)

        return  ResponseEntity.ok(messages.map { MessageResponseMapper.from(it) })
    }

    @PutMapping("/{cid}/block")
    fun blockUser(@PathVariable("cid") id: String): ResponseEntity<FriendProfileResponse>{
        val user = getCurrentUser()
        val friend = chatService.blockConversation(id, user)
        return ResponseEntity.ok( FriendProfileResponse(friend.id, friend.email, friend.name, friend.imgUrl, friend.blockedBy))
    }

    @PutMapping("/{cid}/unblock")
    fun unblockUser(@PathVariable("cid") id: String): ResponseEntity<FriendProfileResponse>{
        val user = getCurrentUser()
        val friend = chatService.unblockConversation(id, user)
        return ResponseEntity.ok( FriendProfileResponse(friend.id, friend.email, friend.name, friend.imgUrl, friend.blockedBy))
    }

    @GetMapping("/{cid}/messages")
    fun getMessages(
            @PathVariable("cid") id: String,
            @RequestParam("from", required = false) from: String?
    ): ResponseEntity<List<MessageResponse>>{
        val user = getCurrentUser()
        val updatedAfter = DateTimeUtil.getDateFromString(from)
        val messages = messageService.getMessagesByChat(id, user.id, updatedAfter)

        return  ResponseEntity.ok(messages.map { MessageResponseMapper.from(it) })
    }

    @PostMapping("/{cid}/messages/text")
    fun createMessageText(
            @PathVariable("cid") id: String,
            @RequestParam("content", required = false) content: String = ""
    ): ResponseEntity<MessageResponse>{
        val user = getCurrentUser()
        val msg = messageService.createMessage(id, user.id, content, emptyList(), ContentType.TEXT)
        return ResponseEntity.ok( MessageResponseMapper.from(msg) )
    }

    @PostMapping("/{cid}/messages/files", consumes = ["multipart/form-data"])
    fun createMessageFile(
            @PathVariable("cid") id: String,
            @RequestParam("content", required = false) content: String = "",
            @RequestParam("files") files: Array<MultipartFile>
    ): ResponseEntity<MessageResponse>{
        val user = getCurrentUser()
        val uploadedFiles = fileStorageService.store(files.toList(), id)
        val msg = messageService.createMessage(id, user.id, content, uploadedFiles, ContentType.FILE)
        return ResponseEntity.ok(MessageResponseMapper.from(msg))

    }

    @PutMapping("/{cid}/messages/read")
    fun readMessage(
            @RequestBody ids: List<String>,
            @PathVariable("cid") convId: String
    ): ResponseEntity<List<MessageResponse>>{
        val user = getCurrentUser()
        val messages = messageService.updateMessages(ids, convId, user.id)
        return ResponseEntity.ok(messages.map { MessageResponseMapper.from(it) })
    }


    fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }

    companion object{
        const val USER_CHAT_BASE_URI = "/api/chat"
    }
}