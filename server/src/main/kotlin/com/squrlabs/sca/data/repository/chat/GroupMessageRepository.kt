package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.GroupMessageEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupMessageRepository: MongoRepository<GroupMessageEntity, String> {
}