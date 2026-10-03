package ru.dolinnyi.secureapi.auth

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.dolinnyi.secureapi.security.JwtService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): TokenResponse {
        // при неверном пароле бросит BadCredentialsException, его ловит ApiExceptionHandler
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        return TokenResponse(jwtService.issue(request.username), jwtService.ttlSeconds)
    }
}

data class LoginRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String,
)

data class TokenResponse(
    val token: String,
    val expiresIn: Long,
    val tokenType: String = "Bearer",
)
