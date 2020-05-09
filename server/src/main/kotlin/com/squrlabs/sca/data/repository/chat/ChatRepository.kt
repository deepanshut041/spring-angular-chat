package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.ChatEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatRepository: MongoRepository<ChatEntity, String> {
    fun findAllByUser1OrUser2AndUpdatedAtAfter(user1: String, user2: String, date: Date): List<ChatEntity>
    fun findByUser1AndUser2OrUser1AndUser2(user1: String, friend2: String, friend1: String, user2: String): Optional<ChatEntity>
}