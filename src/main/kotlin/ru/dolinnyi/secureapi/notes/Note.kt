package ru.dolinnyi.secureapi.notes

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import ru.dolinnyi.secureapi.user.AppUser
import java.time.Instant

@Entity
@Table(name = "notes")
class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, length = 100)
    var title: String,
    @Column(nullable = false, length = 2000)
    var content: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    var owner: AppUser,
)
