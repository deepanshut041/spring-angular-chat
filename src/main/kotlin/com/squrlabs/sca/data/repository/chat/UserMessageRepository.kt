package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.UserMessageEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface UserMessageRepository: MongoRepository<UserMessageEntity, String> {
}