package com.squrlabs.sca.web.controller.user

import com.squrlabs.sca.domain.service.user.UserService
import com.squrlabs.sca.util.auth.util.UserPrincipal
import com.squrlabs.sca.web.controller.user.UserController.Companion.USER_BASE_URI
import com.squrlabs.sca.web.dto.user.UserProfile
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(USER_BASE_URI, consumes = ["application/json"])
@Tag(name = "User Api", description = " This contains url related to login account")
class UserController(
        @Autowired val userService: UserService
) {

    @GetMapping("/me")
    fun getMyProfile(): UserProfile{
        val user = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return this.userService.getUserProfile(user.id).let { UserProfile(it.id, it.email, it.name, it.imgUrl) }
    }

    companion object{
        const val USER_BASE_URI = "/api/user"
    }
}