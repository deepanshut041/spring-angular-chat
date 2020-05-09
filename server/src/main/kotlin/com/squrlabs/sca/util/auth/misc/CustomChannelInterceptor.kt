package com.squrlabs.sca.util.auth.misc

import com.squrlabs.sca.domain.service.user.UserService
import com.squrlabs.sca.util.auth.util.UserPrincipal
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class CustomChannelInterceptor(
        private val tokenProvider: TokenProvider,
        private val userService: UserService
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {

        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
        if (StompCommand.CONNECT == accessor!!.command) {
            val authorization = accessor.getNativeHeader("Authorization")
            val accessToken = getJwtFromRequest(authorization!![0])
            if (!accessToken.isNullOrEmpty() && tokenProvider.validateToken(accessToken)) {
                val userId: String = tokenProvider.getUserIdFromToken(accessToken)
                val userDetails: UserDetails = userService.getUserById(userId)!!
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                accessor.user = authentication
            }
        }
        if (StompCommand.SUBSCRIBE == accessor.command) {
            getDst(accessor.destination!!, "/notifications/")?.let { id ->
                accessor.user?.let { if (!verifyUser(it, id)) return null }
            }
        }
        return message
    }

    private fun getJwtFromRequest(bearerToken: String?): String? {
        bearerToken?.let {
            if (it.startsWith("Bearer "))
                return it.substring(7, it.length)
        }
        return null
    }

    private fun getDst(s: String, prefix: String): String? {
        if (s.startsWith(prefix)) {
            return s.substring(prefix.length, s.length)
        }
        return null
    }

    private fun verifyUser(user: Any, id: String): Boolean {
        ((user as UsernamePasswordAuthenticationToken).principal as UserPrincipal).let {
            return (it.id == id)
        }
    }
}