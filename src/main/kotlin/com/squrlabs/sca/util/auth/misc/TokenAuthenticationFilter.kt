package com.squrlabs.sca.util.auth.misc

import com.squrlabs.sca.domain.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class TokenAuthenticationFilter(
        @Autowired val tokenProvider: TokenProvider,
        @Autowired val userService: UserService
): OncePerRequestFilter(){
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = getJwtFromRequest(request)
            if (!jwt.isNullOrEmpty() && tokenProvider.validateToken(jwt)) {
                val userId: String = tokenProvider.getUserIdFromToken(jwt)
                val userDetails: UserDetails = userService.getUserById(userId)!!
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            logger.error("Could not set user authentication in security context", ex)
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader("Authorization")
        bearerToken?.let{
            if (it.startsWith("Bearer "))
                return it.substring(7, it.length)
        }
        return null
    }

}