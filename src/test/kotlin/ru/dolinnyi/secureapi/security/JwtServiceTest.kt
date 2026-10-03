package ru.dolinnyi.secureapi.security

import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JwtServiceTest {
    private val secret = "test-secret-long-enough-for-hs256-signing"
    private val service = JwtService(JwtProperties(secret, Duration.ofMinutes(5)))

    @Test
    fun `issued token is accepted`() {
        val token = service.issue("alice")
        assertEquals("alice", service.subjectOf(token))
    }

    @Test
    fun `token from another key is rejected`() {
        val other = JwtService(JwtProperties("$secret-other", Duration.ofMinutes(5)))
        assertNull(service.subjectOf(other.issue("alice")))
    }

    @Test
    fun `expired token is rejected`() {
        val expired = JwtService(JwtProperties(secret, Duration.ofMinutes(-5)))
        assertNull(expired.subjectOf(expired.issue("alice")))
    }

    @Test
    fun `garbage is rejected`() {
        assertNull(service.subjectOf("not.a.jwt"))
    }
}
