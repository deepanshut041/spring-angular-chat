package com.squrlabs.sca.domain.service.chat

import com.squrlabs.sca.data.entity.chat.UserMessageEntity
import com.squrlabs.sca.data.entity.chat.UserMessageMapper
import com.squrlabs.sca.data.repository.chat.ConversationRepository
import com.squrlabs.sca.data.repository.chat.UserMessageRepository
import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.UserMessageModel
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

@Service("userMessageService")
class UserMessageServiceImpl(
        @Autowired val simpMessagingTemplate: SimpMessagingTemplate,
        @Autowired val conversationRepository: ConversationRepository,
        @Autowired val userMessageRepository: UserMessageRepository
) : UserMessageService {
    override fun getAllMessages(ids: List<String>, userId: String): List<UserMessageModel> {
        val msgs = ArrayList<UserMessageModel>()
        conversationRepository.findAllById(ids).map { conv->
            if (conv.user1 == userId || conv.user2 == userId) {
                userMessageRepository.findAllByConversationId(conv.id!!).map { msgs.add(UserMessageMapper.to(it)) }
            }
        }
        return msgs
    }
    override fun getMessages(id: String, userId: String, date: Date): List<UserMessageModel> {
        conversationRepository.findById(id).toNullable()?.let { convs ->
            if (convs.user1 == userId || convs.user2 == userId) {
                return userMessageRepository.findAllByConversationIdAndUpdatedAtAfter(id, date).map { UserMessageMapper.to(it) }
            } else {
                throw BadRequestException("Sorry you can't get this conversation message")
            }
        } ?: run {
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun updateMessages(id: String, convId: String, userId: String, receivedAt: Boolean): UserMessageModel {
        conversationRepository.findById(convId).toNullable()?.let { convs ->
            if (convs.user1 == userId || convs.user2 == userId) {
                userMessageRepository.findById(id).toNullable()?.let {
                    if (it.senderId != userId) {
                        val msg = if (!receivedAt) userMessageRepository.save(it.copy(readAt = Date(), updatedAt = Date()))
                        else userMessageRepository.save(it.copy(receivedAt = Date(), updatedAt = Date()))
                        simpMessagingTemplate.convertAndSend(
                                "/notifications/${if (convs.user1 == userId) convs.user2 else convs.user1}",
                                SocketModel(SocketType.USER_MESSAGE_UPDATED, UserMessageMapper.to(msg)))
                        return UserMessageMapper.to(msg)
                    } else {
                        throw BadRequestException("Sorry you can't update this message")
                    }
                } ?: run {
                    throw ResourceNotFoundException("Message", "id", id)
                }
            } else {
                throw BadRequestException("Sorry you can't update this message")
            }
        } ?: run {
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun createMessage(id: String, userId: String, content: String, mediaUrl: String, mediaType: ContentType): UserMessageModel {
        conversationRepository.findById(id).toNullable()?.let {
            if ((it.user1 == userId || it.user2 == userId) && it.blockerId == "") {
                val msg = userMessageRepository.save(UserMessageEntity(null, userId, id, content,
                        mediaUrl, mediaType, Date(), Date(), null, null))
                simpMessagingTemplate.convertAndSend("/notifications/${if (it.user1 == userId) it.user2 else it.user1}",
                        SocketModel(SocketType.USER_MESSAGE_ADDED, UserMessageMapper.to(msg)))
                return UserMessageMapper.to(msg)
            } else {
                throw BadRequestException("Sorry you can't message to this conversation")
            }
        } ?: run {
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

}

interface UserMessageService {
    fun getMessages(id: String, userId: String, date: Date): List<UserMessageModel>
    fun updateMessages(id: String, convId: String, userId: String, receivedAt: Boolean): UserMessageModel
    fun createMessage(id: String, userId: String, content: String, mediaUrl: String, mediaType: ContentType): UserMessageModel
    fun getAllMessages(ids: List<String>, userId: String): List<UserMessageModel>
}