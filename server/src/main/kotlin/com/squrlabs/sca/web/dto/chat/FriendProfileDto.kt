package com.squrlabs.sca.web.dto.chat

data class FriendProfileDto(
        val id: String,
        val email: String,
        val name: String,
        val imgUrl: String,
        val isBlocked: Boolean,
        val blockedBy: String
)