package ru.dolinnyi.secureapi.notes

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface NoteRepository : JpaRepository<Note, Long> {

    // q уходит в базу отдельным параметром, а не склеивается с текстом запроса
    @Query(
        """
        select n from Note n
        where n.owner.username = :username
          and lower(n.title) like lower(concat('%', :q, '%'))
        order by n.createdAt desc
        """
    )
    fun search(username: String, q: String): List<Note>
}
