package com.squrlabs.sca.data.entity.chat

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("groups")
data class GroupEntity(
        @Id val id: String?,
        val name: String,
        val imgUrl: String,
        val createdAt: Date,
        val updatedAt: Date,
        val creator: String,
        val admins: List<String>,
        val participants: List<String>
)