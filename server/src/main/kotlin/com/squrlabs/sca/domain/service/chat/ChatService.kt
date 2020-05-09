package com.squrlabs.sca.domain.service.chat

import com.squrlabs.sca.data.entity.chat.ChatEntity
import com.squrlabs.sca.data.repository.chat.ChatRepository
import com.squrlabs.sca.data.repository.user.UserRepository
import com.squrlabs.sca.domain.model.chat.ChatModel
import com.squrlabs.sca.domain.model.chat.FriendProfileModel
import com.squrlabs.sca.domain.model.socket.SocketModel
import com.squrlabs.sca.domain.model.socket.SocketType
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.ResourceNotFoundException
import com.squrlabs.sca.util.auth.util.UserPrincipal
import com.squrlabs.sca.util.toNullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service("chatService")
class ChatServiceImpl(
        @Autowired val simpMessagingTemplate: SimpMessagingTemplate,
        @Autowired val chatRepository: ChatRepository,
        @Autowired val userRepository: UserRepository
) : ChatService {

    override fun newConversation(user: UserPrincipal, email: String): FriendProfileModel {
        userRepository.findByEmail(email).toNullable()?.let { friend ->
            chatRepository.findByUser1AndUser2OrUser1AndUser2(user.id, friend.id, friend.id, user.id).toNullable()?.let {
                throw BadRequestException("User already exist by email:$email")
            } ?: run {
                val newCov = this.chatRepository.save(ChatEntity(null, user.id, friend.id, "", Date(), Date()))
                simpMessagingTemplate.convertAndSend("/notifications/${friend.id}", SocketModel(SocketType.USER_CONVERSATION_ADDED,
                        FriendProfileModel(newCov.id!!, user.mEmail, user.name, user.mImgUrl, newCov.blockedBy)))
                return FriendProfileModel(newCov.id, friend.email, friend.name, friend.imgUrl, newCov.blockedBy)
            }
        } ?: run {
            throw ResourceNotFoundException("User", "email", email)
        }
    }

    override fun getConversations(id: String, date: Date): List<FriendProfileModel> {
        val friends = HashMap<String, ChatEntity>()
        chatRepository.findAllByUser1OrUser2AndUpdatedAtAfter(id, id, date).map {
            if (it.user1 == id)
                friends[it.user2] = it
            if (it.user2 == id)
                friends[it.user1] = it
        }
        val users = userRepository.findAllById(friends.keys)
        return users.map {
            val profile = friends[it.id]!!
            FriendProfileModel(profile.id!!, it.email, it.name, it.imgUrl, profile.blockedBy)
        }
    }

    override fun blockConversation(id: String, user: UserPrincipal): FriendProfileModel {
        chatRepository.findById(id).toNullable()?.let {
            if ((it.user1 == user.id|| it.user2 == user.id) && it.blockedBy == ""){
                val newCov = chatRepository.save(it.copy(blockedBy = user.id, updatedAt = Date()))
                val friend = userRepository.findById(if (it.user1 == user.id) it.user2 else it.user1).toNullable()!!
                simpMessagingTemplate.convertAndSend("/notifications/${friend.id}", SocketModel(SocketType.USER_CONVERSATION_UPDATED,
                        FriendProfileModel(newCov.id!!, user.mEmail, user.name, user.mImgUrl, newCov.blockedBy)))
                return FriendProfileModel(newCov.id, friend.email, friend.name, friend.imgUrl, newCov.blockedBy)
            } else{
                throw BadRequestException("Sorry you can block this conversation")
            }
        }?: run{
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }

    override fun unblockConversation(id: String, user: UserPrincipal): FriendProfileModel {
        chatRepository.findById(id).toNullable()?.let {
            if ((it.user1 == user.id || it.user2 == user.id) && it.blockedBy == user.id){
                val newCov = chatRepository.save(it.copy(blockedBy = "", updatedAt = Date()))
                val friendId = if (it.user1 == user.id) it.user2 else it.user1
                val friend = userRepository.findById(if (it.user1 == user.id) it.user2 else it.user1).toNullable()!!
                simpMessagingTemplate.convertAndSend("/notifications/${friendId}", SocketModel(SocketType.USER_CONVERSATION_UPDATED,
                        FriendProfileModel(newCov.id!!, user.mEmail, user.name, user.mImgUrl, newCov.blockedBy)))
                return FriendProfileModel(newCov.id, friend.email, friend.name, friend.imgUrl, newCov.blockedBy)
            } else{
                throw BadRequestException("Sorry you can unblock this conversation")
            }
        }?: run{
            throw ResourceNotFoundException("Conversation", "id", id)
        }
    }
    
    override fun getBlockedConversation(): List<ChatModel> {
        return emptyList()
    }
}

interface ChatService {
    fun newConversation(user: UserPrincipal, email: String): FriendProfileModel
    fun getConversations(id: String, date: Date): List<FriendProfileModel>
    fun blockConversation(id: String, user: UserPrincipal): FriendProfileModel
    fun unblockConversation(id: String, user: UserPrincipal): FriendProfileModel
    fun getBlockedConversation(): List<ChatModel>
}
