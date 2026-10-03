package ru.dolinnyi.secureapi.notes

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
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

fun Note.toResponse() = NoteResponse(requireNotNull(id), title, content, createdAt)
