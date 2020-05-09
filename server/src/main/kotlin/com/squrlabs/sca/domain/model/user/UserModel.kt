package com.squrlabs.sca.domain.model.user

data class UserModel(val id: String = "",
                     val name: String,
                     val email: String,
                     val password: String,
                     val imgUrl: String,
                     val emailVerified: Boolean = false,
                     val accountLocked: Boolean = false,
                     val accountExpired: Boolean = false,
                     val credentialsExpired: Boolean = false,
                     val provider: AuthProvider,
                     val providerId: String,
                     val roles: List<String> = emptyList())