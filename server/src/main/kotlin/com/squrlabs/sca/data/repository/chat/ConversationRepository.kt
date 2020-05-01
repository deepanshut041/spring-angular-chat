package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.ConversationEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversationRepository: MongoRepository<ConversationEntity, String> {
}