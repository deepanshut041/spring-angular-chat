package com.squrlabs.sca.web.dto.chat

data class FriendProfileResponse(
        val id: String,
        val email: String,
        val name: String,
        val imgUrl: String,
        val blockedBy: String
)