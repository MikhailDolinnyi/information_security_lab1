package ru.dolinnyi.secureapi.notes

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.dolinnyi.secureapi.user.UserRepository

@Service
class NoteService(
    private val notes: NoteRepository,
    private val users: UserRepository,
) {
    // каждый видит только свои заметки
    fun list(
        username: String,
        q: String?,
    ): List<NoteResponse> = notes.search(username, q.orEmpty().trim()).map { it.toResponse() }

    @Transactional
    fun create(
        username: String,
        request: CreateNoteRequest,
    ): NoteResponse {
        val owner = users.findByUsername(username) ?: error("user $username not found")
        val note = Note(title = request.title.trim(), content = request.content.trim(), owner = owner)
        return notes.save(note).toResponse()
    }
}
