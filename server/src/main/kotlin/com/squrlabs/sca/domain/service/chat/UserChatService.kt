package com.squrlabs.sca.domain.service.chat

import com.squrlabs.sca.data.entity.chat.ConversationEntity
import com.squrlabs.sca.data.entity.chat.UserMessageEntity
import com.squrlabs.sca.data.entity.chat.UserMessageMapper
import com.squrlabs.sca.data.repository.chat.ConversationRepository
import com.squrlabs.sca.data.repository.chat.UserMessageRepository
import com.squrlabs.sca.data.repository.user.UserRepository
import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.ConversationModel
import com.squrlabs.sca.domain.model.chat.FriendProfileModel
import com.squrlabs.sca.domain.model.chat.UserMessageModel
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.ResourceNotFoundException
import com.squrlabs.sca.util.toNullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service("userConversationService")
class UserChatServiceImpl(
        @Autowired val simpMessagingTemplate: SimpMessagingTemplate,
        @Autowired val conversationRepository: ConversationRepository,
        @Autowired val userMessageRepository: UserMessageRepository,
        @Autowired val userRepository: UserRepository
) : UserChatService {

    override fun newConversation(userId: String, email: String) {
        userRepository.findByEmail(email).toNullable()?.let { user ->
            conversationRepository.findByUser1AndUser2OrUser1AndUser2(userId, user.id, user.id, userId).toNullable()?.let {
                throw BadRequestException("User already exist by email:$email")
            } ?: run {
                this.conversationRepository.save(ConversationEntity(null, userId, user.id, false, "", Date(), Date()))
                simpMessagingTemplate.convertAndSend("/topic/$userId", "Update Friend")
                simpMessagingTemplate.convertAndSend("/topic/${user.id}", "Update Friend")
            }
        } ?: run {
            throw ResourceNotFoundException("User", "email", email)
        }

    }

    override fun getConversations(id: String, date: Date): List<FriendProfileModel> {
        val friends = HashMap<String, ConversationEntity>()
        conversationRepository.findAllByUser1OrUser2AndUpdatedAtAfter(id, id, date).map {
            if (it.user1 == id)
                friends[it.user2] = it
            if (it.user2 == id)
                friends[it.user1] = it
        }
        val users = userRepository.findAllById(friends.keys)
        return users.map {
            val profile = friends[it.id]!!
            FriendProfileModel(it.id, it.email, it.name, it.imgUrl, profile.isBlocked, profile.blockerId)
        }
    }

    override fun blockConversation(id: String, userId: String) {
        conversationRepository.findById(id).toNullable()?.let {
            if ((it.user1 == userId || it.user2 == userId) && it.blockerId == ""){
                conversationRepository.save(it.copy(isBlocked = true, blockerId = userId, updatedAt = Date()))
            } else{
                BadRequestException("Sorry you can block this conversation")
            }
        }?: run{
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun unblockConversation(id: String, userId: String) {
        conversationRepository.findById(id).toNullable()?.let {
            if ((it.user1 == userId || it.user2 == userId) && it.blockerId == userId){
                conversationRepository.save(it.copy(isBlocked = false, blockerId = "", updatedAt = Date()))
            } else{
                BadRequestException("Sorry you can unblock this conversation")
            }
        }?: run{
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun getMessages(id: String, userId: String, date: Date): List<UserMessageModel> {
        conversationRepository.findById(id).toNullable()?.let { convs ->
            if (convs.user1 == userId || convs.user2 == userId){
                return userMessageRepository.findAllByConversationIdAndUpdatedAtAfter(id, date).map { UserMessageMapper.to(it) }
            } else{
                throw BadRequestException("Sorry you can't get this conversation message")
            }
        }?: run {
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun updateMessages(id: String, convId: String, userId: String, receivedAt: Boolean) {
        conversationRepository.findById(convId).toNullable()?.let { convs ->
            if (convs.user1 == userId || convs.user2 == userId){
                userMessageRepository.findById(id).toNullable()?.let {
                    if (it.senderId != userId && !receivedAt)
                        userMessageRepository.save(it.copy(readAt = Date(), updatedAt = Date()))
                    if (it.senderId != userId && receivedAt)
                        userMessageRepository.save(it.copy(receivedAt = Date(), updatedAt = Date()))
                } ?: run{
                    throw ResourceNotFoundException("Message", "id", id)
                }
            } else{
                throw BadRequestException("Sorry you can't update this message")
            }
        }?: run {
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun createMessage(id: String, userId: String, content: String, mediaUrl: String, mediaType: ContentType) {
        conversationRepository.findById(id).toNullable()?.let { convs ->
            if ((convs.user1 == userId || convs.user2 == userId) && convs.blockerId == ""){
               userMessageRepository.save(UserMessageEntity(
                       null, userId, id, content, mediaUrl, mediaType, Date(), Date(), null, null
               ))
            } else{
                throw BadRequestException("Sorry you can't message to this conversation")
            }
        }?: run {
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun getBlockedConversation(): List<ConversationModel> {
        return emptyList()
    }
}

interface UserChatService {
    fun newConversation(userId: String, email: String)
    fun getConversations(id: String, date: Date): List<FriendProfileModel>
    fun getMessages(id: String, userId: String, date: Date): List<UserMessageModel>
    fun updateMessages(id: String, convId: String, userId: String, receivedAt: Boolean)
    fun createMessage(id: String, userId:String, content: String, mediaUrl: String, mediaType: ContentType)
    fun blockConversation(id: String, userId: String)
    fun unblockConversation(id: String, userId: String)
    fun getBlockedConversation(): List<ConversationModel>
}
