package com.squrlabs.sca.util.auth.util

import com.squrlabs.sca.domain.model.user.UserModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(val id: String, val mEmail: String, val mPassword: String, val mAuthorities: Collection<GrantedAuthority?>,
                    val mAttributes: Map<String, Any>, val mName: String, val mImgUrl: String) : OAuth2User, UserDetails {

    companion object {
        fun create(userModel: UserModel, attributes: Map<String, Any>, authorities: Collection<GrantedAuthority?>): UserPrincipal {
            return UserPrincipal(id = userModel.id, mEmail = userModel.email, mPassword = userModel.password,
                    mAttributes = attributes, mAuthorities = authorities, mName = userModel.name, mImgUrl = userModel.imgUrl)
        }
    }


    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return mEmail
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return mPassword
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return mAuthorities
    }

    override fun getName(): String {
        return mName
    }

    override fun getAttributes(): Map<String, Any> {
        return mAttributes
    }
}