package com.squrlabs.sca.config

import com.squrlabs.sca.domain.service.auth.CustomOAuth2UserService
import com.squrlabs.sca.domain.service.user.UserService
import com.squrlabs.sca.util.auth.misc.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
class SecurityConfigure(
        @Autowired val userService: UserService,
        @Autowired val tokenAuthenticationFilter: TokenAuthenticationFilter,
        @Autowired val customOAuth2UserService: CustomOAuth2UserService,
        @Autowired val oAuth2SuccessHandler: OAuth2SuccessHandler,
        @Autowired val oAuth2RequestRepository: OAuth2RequestRepository,
        @Autowired val oAuth2FailureHandler: OAuth2FailureHandler
): WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Throws(java.lang.Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
                .userDetailsService<UserDetailsService>(userService)
                .passwordEncoder(passwordEncoder())
    }


    override fun configure(http: HttpSecurity) {
        http.cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(RestAuthenticationEntryPoint())
                .and().authorizeRequests()
                .antMatchers("/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .antMatchers("/api/account/**", "/api/docs", "/login/oauth2/code/**", "/ws/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login { oauth2Login ->
                    oauth2Login.authorizationEndpoint {
                        it.authorizationRequestRepository(oAuth2RequestRepository)
                    }
                    oauth2Login.userInfoEndpoint {
                            it.userService(customOAuth2UserService)
                    }
                    oauth2Login.successHandler(oAuth2SuccessHandler).failureHandler(oAuth2FailureHandler)
                }

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

}