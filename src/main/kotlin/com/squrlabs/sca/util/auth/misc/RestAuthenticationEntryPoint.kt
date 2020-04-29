package com.squrlabs.sca.util.auth.misc

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RestAuthenticationEntryPoint: AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        response!!.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException!!.localizedMessage)
    }
}