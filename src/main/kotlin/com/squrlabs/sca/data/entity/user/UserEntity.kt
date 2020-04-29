package com.squrlabs.sca.data.entity.user

import com.squrlabs.sca.domain.model.user.AuthProvider
import com.squrlabs.sca.domain.model.user.UserModel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class UserEntity(@Id val id: String = "",
                      val name: String,
                      @Indexed(unique = true) val email: String,
                      val password: String,
                      val imgUrl: String,
                      val emailVerified: Boolean = false,
                      val accountLocked: Boolean = false,
                      val accountExpired: Boolean = false,
                      val credentialsExpired: Boolean = false,
                      val provider: AuthProvider,
                      val providerId: String,
                      val roles: List<String> = emptyList())

object UserEntityMapper {
    fun from(user: UserModel): UserEntity = UserEntity(
            id = user.id,
            email = user.email,
            name = user.name,
            password = user.password,
            imgUrl = user.imgUrl,
            emailVerified = user.emailVerified,
            accountLocked = user.accountLocked,
            accountExpired = user.accountExpired,
            credentialsExpired = user.credentialsExpired,
            provider = user.provider,
            providerId = user.providerId,
            roles = user.roles
    )

    fun to(user: UserEntity): UserModel = UserModel(
            id = user.id,
            email = user.email,
            name = user.name,
            password = user.password,
            imgUrl = user.imgUrl,
            emailVerified = user.emailVerified,
            accountLocked = user.accountLocked,
            accountExpired = user.accountExpired,
            credentialsExpired = user.credentialsExpired,
            provider = user.provider,
            providerId = user.providerId,
            roles = user.roles
    )
}