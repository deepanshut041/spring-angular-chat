package com.squrlabs.sca.util

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}

class OAuth2AuthenticationProcessingException : AuthenticationException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
data class ResourceNotFoundException(val resourceName: String, val fieldName: String, val fieldValue: Any) : RuntimeException("$resourceName not found with $fieldName: $fieldValue")