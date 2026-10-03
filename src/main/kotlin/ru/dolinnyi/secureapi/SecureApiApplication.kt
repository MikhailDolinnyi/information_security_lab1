package ru.dolinnyi.secureapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecureApiApplication

fun main(args: Array<String>) {
    runApplication<SecureApiApplication>(*args)
}
