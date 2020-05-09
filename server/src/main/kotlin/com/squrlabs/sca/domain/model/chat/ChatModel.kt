package com.squrlabs.sca.domain.model.chat

import java.util.*

data class ChatModel(
        val id:String?,
        val user1:String,
        val user2: String,
        val blockedBy: String,
        val createdAt: Date,
        val updatedAt: Date
)