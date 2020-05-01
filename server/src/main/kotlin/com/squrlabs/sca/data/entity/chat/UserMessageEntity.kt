package com.squrlabs.sca.data.entity.chat

import com.squrlabs.sca.domain.model.chat.ContentType
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
        val receivedBy: Boolean,
        val readBy: Boolean,
        val pending: Boolean
)