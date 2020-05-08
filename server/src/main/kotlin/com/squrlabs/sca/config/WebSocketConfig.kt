package com.squrlabs.sca.config

import com.squrlabs.sca.domain.service.user.UserService
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.auth.misc.TokenProvider
import com.squrlabs.sca.util.auth.util.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
        @Autowired val tokenProvider: TokenProvider,
        @Autowired val userService: UserService
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/notifications")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS()
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor{
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
                if(StompCommand.SUBSCRIBE == accessor.command){
                    getDst(accessor.destination!!, "/notifications/")?.let {id ->
                        accessor.user?.let{
                            if(!verifyUser(it, id)) throw BadRequestException("Sorry unauthorised access")
                            else println("Verified")
                        }
                    }
                }
                return message
            }
        })
    }

    private fun getJwtFromRequest(bearerToken: String?): String? {
        bearerToken?.let{
            if (it.startsWith("Bearer "))
                return it.substring(7, it.length)
        }
        return null
    }

    private fun getDst(s: String, prefix: String): String?{
        if (s.startsWith(prefix)){
            return s.substring(prefix.length, s.length)
        }
        return  null
    }

    private fun verifyUser(user: Any, id: String): Boolean{
        ((user as  UsernamePasswordAuthenticationToken).principal as UserPrincipal).let {
            return (it.id == id)
        }
    }
}