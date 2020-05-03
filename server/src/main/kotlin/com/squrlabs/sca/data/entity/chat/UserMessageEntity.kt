package com.squrlabs.sca.data.entity.chat

import com.squrlabs.sca.data.entity.user.UserEntity
import com.squrlabs.sca.domain.model.chat.ContentType
import com.squrlabs.sca.domain.model.chat.UserMessageModel
import com.squrlabs.sca.domain.model.user.UserModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("user_messages")
data class UserMessageEntity(
        @Id val id:String?,
        val senderId: String,
        @Indexed(unique = false) val conversationId: String,
        val content: String,
        val mediaUrl: String,
        val contentType: ContentType,
        val createdAt: Date,
        val updatedAt: Date,
        val receivedAt: Date? = null,
        val readAt: Date? = null
)

object UserMessageMapper{
    fun from(it: UserMessageModel, id: String?) = UserMessageEntity(
            id = id,
            senderId = it.senderId,
            conversationId = it.conversationId,
            content = it.content,
            mediaUrl = it.mediaUrl,
            contentType = it.contentType,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt,
            receivedAt = it.receivedAt,
            readAt = it.readAt
    )

    fun to(it: UserMessageEntity) = UserMessageModel(
            id = it.id!!,
            senderId = it.senderId,
            conversationId = it.conversationId,
            content = it.content,
            mediaUrl = it.mediaUrl,
            contentType = it.contentType,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt,
            receivedAt = it.receivedAt,
            readAt = it.readAt
    )


}