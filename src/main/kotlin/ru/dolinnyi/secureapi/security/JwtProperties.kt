package ru.dolinnyi.secureapi.security

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("jwt")
data class JwtProperties(
    val secret: String = "",
    val ttl: Duration = Duration.ofHours(1),
)
