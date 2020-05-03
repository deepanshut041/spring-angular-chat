package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.UserMessageEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserMessageRepository: MongoRepository<UserMessageEntity, String> {

    fun findAllByConversationIdAndUpdatedAtAfter(id: String, date: Date): List<UserMessageEntity>
}