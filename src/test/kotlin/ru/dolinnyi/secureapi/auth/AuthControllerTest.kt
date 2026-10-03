package ru.dolinnyi.secureapi.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest(
    @Autowired val mockMvc: MockMvc,
) {
    @Test
    fun `valid credentials return token`() {
        login("alice", "Alice#2026").andExpect {
            status { isOk() }
            jsonPath("$.token") { isNotEmpty() }
            jsonPath("$.tokenType") { value("Bearer") }
        }
    }

    @Test
    fun `wrong password is rejected`() {
        login("alice", "wrong").andExpect {
            status { isUnauthorized() }
            jsonPath("$.error") { value("invalid username or password") }
        }
    }

    @Test
    fun `unknown user gets the same answer as wrong password`() {
        login("eve", "whatever").andExpect {
            status { isUnauthorized() }
            jsonPath("$.error") { value("invalid username or password") }
        }
    }

    @Test
    fun `blank fields are rejected`() {
        login("", "").andExpect {
            status { isBadRequest() }
            jsonPath("$.error") { value("validation failed") }
        }
    }

    private fun login(
        username: String,
        password: String,
    ) = mockMvc.post("/auth/login") {
        contentType = MediaType.APPLICATION_JSON
        content = """{"username":"$username","password":"$password"}"""
    }
}
