package ru.dolinnyi.secureapi.notes

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.util.HtmlUtils
import java.time.Instant

data class CreateNoteRequest(
    @field:NotBlank @field:Size(max = 100) val title: String,
    @field:NotBlank @field:Size(max = 2000) val content: String,
)

data class NoteResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Instant,
)

// текст заметки пишет пользователь, клиент может вставить его в html как есть,
// поэтому < > & " ' экранируем на выходе
fun Note.toResponse() =
    NoteResponse(
        id = requireNotNull(id),
        title = HtmlUtils.htmlEscape(title),
        content = HtmlUtils.htmlEscape(content),
        createdAt = createdAt,
    )
