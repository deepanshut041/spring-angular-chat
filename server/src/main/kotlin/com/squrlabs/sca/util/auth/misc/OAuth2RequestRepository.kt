package com.squrlabs.sca.util.auth.misc

import com.squrlabs.sca.util.CookieUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2RequestRepository: AuthorizationRequestRepository<OAuth2AuthorizationRequest>{

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)?.let {
            return CookieUtils.deserialize(it, OAuth2AuthorizationRequest::class.java)
        }
        return null
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URL_PARAM_COOKIE_NAME);
    }

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, request: HttpServletRequest, response: HttpServletResponse) {
        authorizationRequest?.let {
            CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds)
            request.getParameter(REDIRECT_URL_PARAM_COOKIE_NAME)?.let { uri ->
                if (uri.isNotBlank())
                    CookieUtils.addCookie(response, REDIRECT_URL_PARAM_COOKIE_NAME, uri, cookieExpireSeconds)
            }

        }?: run{
            removeAuthorizationRequestCookies(request, response)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return this.loadAuthorizationRequest(request)
    }

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URL_PARAM_COOKIE_NAME = "redirect_url"
        private const val cookieExpireSeconds = 180
    }
}