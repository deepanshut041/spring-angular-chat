package com.squrlabs.sca.domain.service.user

import com.squrlabs.sca.data.entity.user.UserEntityMapper
import com.squrlabs.sca.data.repository.user.UserRepository
import com.squrlabs.sca.domain.model.user.UserModel
import com.squrlabs.sca.util.ResourceNotFoundException
import com.squrlabs.sca.util.auth.util.UserPrincipal
import com.squrlabs.sca.util.toNullable
import com.squrlabs.sca.web.dto.user.UserProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("userService")
class UserServiceImpl(
        @Autowired val userRepository: UserRepository
        ): UserService {

    @Throws(UsernameNotFoundException::class)
    override fun getUserById(id: String): UserDetails? {
        userRepository.findById(id).map(UserEntityMapper::to).toNullable()?.let {
            return UserPrincipal.create(it, emptyMap(), rolesToAuthority(it.roles))
        }
        throw ResourceNotFoundException("User", "id", id)
    }



    @Throws(UsernameNotFoundException::class)
    override fun getUserByEmail(email: String): UserDetails? {
       userRepository.findByEmail(email).map(UserEntityMapper::to).toNullable()?.let {
            return UserPrincipal.create(it, emptyMap(), rolesToAuthority(it.roles))
        }
        throw ResourceNotFoundException("User", "email", email)
    }

    override fun getUserProfile(id: String): UserModel {
        userRepository.findById(id).map(UserEntityMapper::to).toNullable()?.let {
            return it
        }?: run{
            throw ResourceNotFoundException("User", "id", id)
        }
    }

    override fun saveUser(model: UserModel): UserModel {
        return UserEntityMapper.to(userRepository.save(UserEntityMapper.from(model)))
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        return getUserByEmail(username)
    }

}

interface UserService: UserDetailsService {
    fun getUserById(id: String): UserDetails?
    fun getUserByEmail(email: String): UserDetails?
    fun saveUser(model: UserModel): UserModel
    fun existsByEmail(email: String): Boolean
    fun getUserProfile(id: String): UserModel
}

fun rolesToAuthority(roles: List<String>): Collection<GrantedAuthority>{
    val authorises = ArrayList<GrantedAuthority>()

    roles.map {
        authorises.add(SimpleGrantedAuthority(it))
    }
    return authorises
}
