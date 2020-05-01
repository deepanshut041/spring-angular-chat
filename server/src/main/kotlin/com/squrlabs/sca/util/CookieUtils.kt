package com.squrlabs.sca.util

import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


object CookieUtils {
    fun getCookie(request: HttpServletRequest, name: String?): Cookie? {
        val cookies = request.cookies
        cookies?.map {
            if (it.name == name)
                return it
        }
        return null
    }

    fun addCookie(response: HttpServletResponse, name: String?, value: String?, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String?) {
        val cookies = request.cookies
        cookies?.map {
            if (it.name == name){
                it.value = ""
                it.path = "/"
                it.maxAge = 0
                response.addCookie(it)
            }
        }
    }

    fun serialize(`object`: Any?): String {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(`object`))
    }

    fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.value)))
    }
}