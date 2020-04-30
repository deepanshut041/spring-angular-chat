package com.squrlabs.sca.domain.model.chat

import java.util.*

data class GroupMessageModel(
        val id:String?,
        val senderId: String,
        val conversationId: String,
        val content: String,
        val mediaUrl: String,
        val contentType: ContentType,
        val createdAt: Date,
        val receivedBy: List<String>,
        val readBy: List<String>,
        val pending: List<String>
)