package com.squrlabs.sca.data.entity.chat

import com.squrlabs.sca.domain.model.chat.ContentType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("group_messages")
data class GroupMessageEntity(
        @Id val id:String?,
        val senderId: String,
        @Indexed val conversationId: String,
        val content: String,
        val mediaUrl: String,
        val contentType: ContentType,
        val createdAt: Date,
        val receivedBy: List<String>,
        val readBy: List<String>,
        val pending: List<String>
)