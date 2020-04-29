package com.squrlabs.sca.web.dto.auth

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpRequest(
        @NotBlank val name:String,
        @NotBlank @Email val email:String,
        @NotBlank val password: String
        )