package com.squrlabs.sca.data.entity.chat

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("participants")
data class ParticipantEntity(
        @Id val id: String?,
        @Indexed val userId: String,
        @Indexed val groupId: String,
        val isRemoved: Boolean,
        val removedBy: String
)