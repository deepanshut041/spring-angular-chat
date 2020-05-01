package com.squrlabs.sca.domain.model.chat


data class ParticipantModel(
        val id: String?,
        val userId: String,
        val groupId: String,
        val isRemoved: Boolean,
        val removedBy: String
)