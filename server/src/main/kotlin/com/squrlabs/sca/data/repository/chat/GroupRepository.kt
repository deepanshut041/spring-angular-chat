package com.squrlabs.sca.data.repository.chat

import com.squrlabs.sca.data.entity.chat.GroupEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository: MongoRepository<GroupEntity, String> {
}