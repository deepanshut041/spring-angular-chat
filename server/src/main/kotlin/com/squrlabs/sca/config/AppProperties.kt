package com.squrlabs.sca.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.*


@ConfigurationProperties(prefix = "app")
object AppProperties{
    val auth = Auth()

    data class Auth(var tokenSecret: String? = null, var tokenExpirationMsec: Long = 0)

    val oauth2 = OAuth2()

    data class OAuth2(var authorizedRedirectUrls: List<String> = ArrayList()){
        fun authorizedRedirectUrls(authorizedRedirectUrls: List<String>): OAuth2 {
            println(authorizedRedirectUrls.toString())
            this.authorizedRedirectUrls = authorizedRedirectUrls
            return this
        }
    }
}