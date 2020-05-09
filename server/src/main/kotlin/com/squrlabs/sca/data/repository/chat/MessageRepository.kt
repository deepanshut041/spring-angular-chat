package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.MessageEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface MessageRepository: MongoRepository<MessageEntity, String> {
    fun findAllByConversationIdAndUpdatedAtAfter(id: String, date: Date): List<MessageEntity>
    fun findAllByConversationId(id: String): List<MessageEntity>
}