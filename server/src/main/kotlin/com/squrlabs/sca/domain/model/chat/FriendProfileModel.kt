package com.squrlabs.sca.domain.model.chat

data class FriendProfileModel(
        val id: String,
        val email: String,
        val name: String,
        val imgUrl: String,
        val blockedBy: String
)