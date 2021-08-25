package io.reflectoring.buckpal.adapter.persistence

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
class TestApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(TestApplication::class.java, *args)
    }
}