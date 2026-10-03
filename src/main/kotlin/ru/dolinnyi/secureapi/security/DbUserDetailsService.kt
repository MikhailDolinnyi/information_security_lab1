package ru.dolinnyi.secureapi.security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.dolinnyi.secureapi.user.UserRepository

@Service
class DbUserDetailsService(
    private val users: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            users.findByUsername(username)
                ?: throw UsernameNotFoundException("user not found")
        return User
            .withUsername(user.username)
            .password(user.passwordHash)
            .roles("USER")
            .build()
    }
}
