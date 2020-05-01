package com.squrlabs.sca.util.auth.util

import com.squrlabs.sca.domain.model.user.AuthProvider
import com.squrlabs.sca.util.OAuth2AuthenticationProcessingException


abstract class OAuth2UserInfo(val attributes: Map<String, Any>) {
    abstract fun getId(): String
    abstract fun getName(): String
    abstract fun getEmail(): String
    abstract fun getImageUrl(): String?
}

class FacebookOAuth2UserInfo(attributes: Map<String, Any>): OAuth2UserInfo(attributes){
    override fun getId(): String  = attributes["id"] as String
    override fun getName(): String = attributes["name"] as String
    override fun getEmail(): String = attributes["email"] as String
    override fun getImageUrl(): String? {
        if (attributes.containsKey("picture")) {
            val pictureObj = attributes["picture"] as Map<String, Any?>?
            if (pictureObj!!.containsKey("data")) {
                val dataObj = pictureObj["data"] as Map<String, Any?>?
                if (dataObj!!.containsKey("url")) {
                    return (dataObj["url"] as String?)!!
                }
            }
        }
        return null
    }

}

class GoogleOAuth2UserInfo(attributes: Map<String, Any>): OAuth2UserInfo(attributes){
    override fun getId(): String  = attributes["sub"] as String
    override fun getName(): String = attributes["name"] as String
    override fun getEmail(): String = attributes["email"] as String
    override fun getImageUrl(): String? = attributes["picture"] as String

}

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return if (registrationId.equals(AuthProvider.google.toString(), ignoreCase = true)) {
            GoogleOAuth2UserInfo(attributes)
        } else if (registrationId.equals(AuthProvider.facebook.toString(), ignoreCase = true)) {
            FacebookOAuth2UserInfo(attributes)
        }  else {
            throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
        }
    }
}