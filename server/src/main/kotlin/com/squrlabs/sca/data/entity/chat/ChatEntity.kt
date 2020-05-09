package com.squrlabs.sca.data.entity.chat

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("chats")
@CompoundIndexes(*[
    CompoundIndex(name = "user1_user2", def = "{'user1' : 1, 'user2': 1}")
])
data class ChatEntity(
        @Id val id:String?,
        @Indexed(unique = false) val user1:String,
        @Indexed(unique = false) val user2: String,
        val blockedBy: String,
        val createdAt: Date,
        val updatedAt: Date
)
