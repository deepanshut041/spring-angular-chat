package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.ParticipantEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository: MongoRepository<ParticipantEntity, String> {
}