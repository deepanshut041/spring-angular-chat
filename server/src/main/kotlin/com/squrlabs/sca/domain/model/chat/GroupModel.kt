package com.squrlabs.sca.domain.model.chat

import java.util.*

data class GroupModel(
        val id: String?,
        val name: String,
        val imgUrl: String,
        val createdAt: Date,
        val updatedAt: Date,
        val creator: String,
        val admins: List<String>,
        val participants: List<String>
)