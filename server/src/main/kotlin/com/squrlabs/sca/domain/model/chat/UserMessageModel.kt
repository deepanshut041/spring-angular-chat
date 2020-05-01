package com.squrlabs.sca.domain.model.chat

import java.util.*

data class UserMessageModel(
        val id:String?,
        val senderId: String,
        val conversationId: String,
        val content: String,
        val mediaUrl: String,
        val contentType: ContentType,
        val createdAt: Date,
        val receivedBy: Boolean,
        val readBy: Boolean,
        val pending: Boolean
)