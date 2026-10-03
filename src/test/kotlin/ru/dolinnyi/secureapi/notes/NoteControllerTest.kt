package ru.dolinnyi.secureapi.notes

import io.jsonwebtoken.Jwts
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.dolinnyi.secureapi.security.JwtService

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val jwtService: JwtService,
) {
    @Test
    fun `request without token is rejected`() {
        mockMvc.get("/api/data").andExpect {
            status { isUnauthorized() }
            jsonPath("$.error") { value("unauthorized") }
        }
    }

    @Test
    fun `token signed with another key is rejected`() {
        val forged =
            Jwts
                .builder()
                .subject("alice")
                .signWith(
                    Jwts.SIG.HS256
                        .key()
                        .build(),
                ).compact()
        mockMvc
            .get("/api/data") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $forged")
            }.andExpect {
                status { isUnauthorized() }
            }
    }

    @Test
    fun `user sees only own notes`() {
        mockMvc
            .get("/api/data") {
                bearer("alice")
            }.andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(2) }
                jsonPath("$[*].title") { value(containsInAnyOrder("Welcome", "Shopping")) }
            }
    }

    @Test
    fun `search filters by title`() {
        mockMvc
            .get("/api/data") {
                bearer("alice")
                param("q", "shop")
            }.andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(1) }
                jsonPath("$[0].title") { value("Shopping") }
            }
    }

    @Test
    fun `sql injection in search returns nothing`() {
        mockMvc
            .get("/api/data") {
                bearer("alice")
                param("q", "' or 1=1 --")
            }.andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(0) }
            }
    }

    @Test
    fun `html in note is escaped in response`() {
        mockMvc
            .post("/api/data") {
                bearer("bob")
                contentType = MediaType.APPLICATION_JSON
                content = """{"title":"xss","content":"<script>alert(1)</script>"}"""
            }.andExpect {
                status { isCreated() }
                jsonPath("$.title") { value("xss") }
                jsonPath("$.content") { value("&lt;script&gt;alert(1)&lt;/script&gt;") }
            }
    }

    @Test
    fun `blank title is rejected`() {
        mockMvc
            .post("/api/data") {
                bearer("bob")
                contentType = MediaType.APPLICATION_JSON
                content = """{"title":"   ","content":"text"}"""
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.error") { value("validation failed") }
            }
    }

    private fun MockHttpServletRequestDsl.bearer(username: String) {
        header(HttpHeaders.AUTHORIZATION, "Bearer ${jwtService.issue(username)}")
    }
}
