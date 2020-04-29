package com.squrlabs.sca.data.repository.user

import com.squrlabs.sca.data.entity.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: MongoRepository<UserEntity, String> {
    fun findByEmail(email: String): Optional<UserEntity>
    fun existsByEmail(email: String): Boolean
}