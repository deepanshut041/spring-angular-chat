package com.squrlabs.sca.util.auth.misc

import com.squrlabs.sca.config.AppProperties
import com.squrlabs.sca.util.auth.util.UserPrincipal
import io.jsonwebtoken.*
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenProvider(private val appProperties: AppProperties) {

    fun createToken(authentication: Authentication): String {
        val userPrincipal: UserPrincipal = authentication.principal as UserPrincipal
        return generateToken(userPrincipal)
    }

    fun generateToken(userPrincipal: UserPrincipal): String {
        val now = Date()
        val expiryDate = Date(now.time + appProperties.auth.tokenExpirationMsec)
        return Jwts.builder()
                .setSubject(userPrincipal.id)
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.auth.tokenSecret)
                .compact()
    }

    fun getUserIdFromToken(token: String?): String {
        return Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(token).body.subject
    }

    fun validateToken(authToken: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(authToken)
            true
        } catch (ex: Exception) {
            false
        }

    }


}
