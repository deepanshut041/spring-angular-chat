package com.squrlabs.sca.util.auth.misc

import com.squrlabs.sca.config.AppProperties
import com.squrlabs.sca.util.BadRequestException
import com.squrlabs.sca.util.CookieUtils.getCookie
import com.squrlabs.sca.util.auth.misc.OAuth2RequestRepository.Companion.REDIRECT_URL_PARAM_COOKIE_NAME
import com.squrlabs.sca.util.auth.util.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2SuccessHandler(@Autowired val tokenProvider: TokenProvider,
                           @Autowired private val appProperties: AppProperties,
                           @Autowired val oAuth2RequestRepository: OAuth2RequestRepository)
    : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication): String {

        val redirectUrl: String = getCookie(request!!, REDIRECT_URL_PARAM_COOKIE_NAME)?.let { return@let it.value }?:""

        if (redirectUrl.isEmpty() && !isAuthorizedRedirectUrl(redirectUrl)) {
            throw BadRequestException("Sorry! We've got an Unauthorized Redirect URL and can't proceed with the authentication")
        }

        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token", tokenProvider.createToken(authentication))
                .build().toUriString()
    }

    private fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        oAuth2RequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUrl(uri:String): Boolean {

        val clientRedirectUri = URI.create(uri)
        appProperties.oauth2.authorizedRedirectUrls.map{
            val authorizedURL = URI.create(it)
            if(authorizedURL.host.toUpperCase() == clientRedirectUri.host.toUpperCase()) {
                return true
            }
        }
        return false
    }

}

@Component
class OAuth2FailureHandler(@Autowired val oAuth2RequestRepository: OAuth2RequestRepository)
    : SimpleUrlAuthenticationFailureHandler() {


    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        var targetUrl: String = getCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)?.value ?: "/"
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.localizedMessage)
                .build().toUriString()
        oAuth2RequestRepository.removeAuthorizationRequestCookies(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}
