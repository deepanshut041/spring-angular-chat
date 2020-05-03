package com.squrlabs.sca.domain.model.chat

import java.util.*

data class ConversationModel(
        val id:String?,
        val user1:String,
        val user2: String,
        val isBlocked: Boolean,
        val blockerId: String,
        val createdAt: Date,
        val updatedAt: Date
)