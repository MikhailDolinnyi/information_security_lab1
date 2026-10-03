package ru.dolinnyi.secureapi.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(private val props: JwtProperties) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val key: SecretKey = if (props.secret.isNotBlank()) {
        Keys.hmacShaKeyFor(props.secret.toByteArray())
    } else {
        // без JWT_SECRET ключ случайный, токены живут до перезапуска
        log.warn("JWT_SECRET is not set, using a random key")
        Jwts.SIG.HS256.key().build()
    }

    private val parser = Jwts.parser().verifyWith(key).build()

    val ttlSeconds: Long
        get() = props.ttl.seconds

    fun issue(username: String): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(props.ttl)))
            .signWith(key)
            .compact()
    }

    // null, если подпись не сошлась, токен протух или это вообще не jwt
    fun subjectOf(token: String): String? = try {
        parser.parseSignedClaims(token).payload.subject
    } catch (e: JwtException) {
        log.debug("rejected token: {}", e.message)
        null
    }
}
