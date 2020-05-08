package com.squrlabs.sca.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.SimpMessageType.*;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer

@Configuration
class WebSocketSecurityConfig:AbstractSecurityWebSocketMessageBrokerConfigurer() {

    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry?) {
        messages!!.simpTypeMatchers(CONNECT, UNSUBSCRIBE, DISCONNECT, HEARTBEAT).permitAll()
                .simpSubscribeDestMatchers("/notifications/**").authenticated()
                .anyMessage().authenticated()
    }

    override fun sameOriginDisabled(): Boolean {
        return true
    }
}