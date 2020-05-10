package com.squrlabs.sca.domain.service.chat

import com.squrlabs.sca.data.entity.chat.FileMapper
import com.squrlabs.sca.data.entity.chat.MessageEntity
import com.squrlabs.sca.data.entity.chat.MessageMapper
import com.squrlabs.sca.data.repository.chat.ChatRepository
import com.squrlabs.sca.data.repository.chat.MessageRepository
import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.FileModel
import com.squrlabs.sca.domain.model.chat.MessageModel
import com.squrlabs.sca.domain.model.socket.SocketModel
import com.squrlabs.sca.domain.model.socket.SocketType
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.ResourceNotFoundException
import com.squrlabs.sca.util.toNullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service("messageService")
class MessageServiceImpl(
        @Autowired val simpMessagingTemplate: SimpMessagingTemplate,
        @Autowired val chatRepository: ChatRepository,
        @Autowired val messageRepository: MessageRepository
) : MessageService {

    override fun getAllMessages(ids: List<String>, userId: String, updatedAt:Date): List<MessageModel> {
        val msgs = ArrayList<MessageModel>()
        chatRepository.findAllById(ids).map { chat -> 
            if (chat.user1 != userId && chat.user2 != userId) return@map null
            messageRepository.findAllByConversationIdAndUpdatedAtAfter(chat.id!!, updatedAt).map { 
                msgs.add(MessageMapper.to(it))
            }
        }
        return msgs
    }

    override fun getMessagesByChat(id: String, userId: String, updatedAt: Date): List<MessageModel> {
        chatRepository.findById(id).toNullable()?.let { chat ->
            if (chat.user1 == userId || chat.user2 == userId) {
                return messageRepository.findAllByConversationIdAndUpdatedAtAfter(id, updatedAt).map { MessageMapper.to(it) }
            } 
        } 
        return emptyList()
    }

    override fun updateMessages(ids: List<String>, chatId: String, userId: String): List<MessageModel> {
        val updatedMessages = ArrayList<MessageModel>()
        var friendId: String? = null
        chatRepository.findById(chatId).toNullable()?.let { chat ->
            if (chat.user1 != userId && chat.user2 != userId) return emptyList()
            friendId = if (chat.user1 == userId) chat.user2 else chat.user1
            val messages = this.messageRepository.findAllById(ids).filter { it.senderId != userId }
            this.messageRepository.saveAll(messages.map { it.copy(read = true, updatedAt = Date()) })
                    .map { updatedMessages.add(MessageMapper.to(it)) }
        }

        friendId?.let { simpMessagingTemplate.convertAndSend("/notifications/${it}",
                    SocketModel(SocketType.USER_MESSAGE_UPDATED, updatedMessages))
        }
        return updatedMessages
    }

    override fun createMessage(chatId: String, userId: String, content: String, files: List<FileModel>, contentType: ContentType): MessageModel {
        chatRepository.findById(chatId).toNullable()?.let {
            if ((it.user1 == userId || it.user2 == userId) && it.blockedBy == "") {
                val msg = messageRepository.save(MessageEntity(null, userId, chatId, content,
                        files.map{ file -> FileMapper.from(file)}, contentType, Date(), Date(), false))
                simpMessagingTemplate.convertAndSend("/notifications/${if (it.user1 == userId) it.user2 else it.user1}",
                        SocketModel(SocketType.USER_MESSAGE_ADDED, MessageMapper.to(msg)))
                return MessageMapper.to(msg)
            } else {
                throw BadRequestException("Sorry you're blocked by user")
            }
        } ?: run {
            throw ResourceNotFoundException("Conversation", "id", chatId)
        }
    }
}

interface MessageService {
    fun getMessagesByChat(id: String, userId: String, updatedAt: Date): List<MessageModel>
    fun getAllMessages(ids: List<String>, userId: String, updatedAt: Date): List<MessageModel>
    fun updateMessages(ids: List<String>, chatId: String, userId: String): List<MessageModel>
    fun createMessage(chatId: String, userId: String, content: String, files: List<FileModel>, contentType: ContentType): MessageModel
}