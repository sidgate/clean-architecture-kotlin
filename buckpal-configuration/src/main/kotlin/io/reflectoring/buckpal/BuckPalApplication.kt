package io.reflectoring.buckpal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BuckPalApplication
fun main(args: Array<String>) {
    SpringApplication.run(BuckPalApplication::class.java, *args)
}
