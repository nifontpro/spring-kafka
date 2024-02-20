package ru.nb.sprkafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SprKafkaApplication

fun main(args: Array<String>) {
	runApplication<SprKafkaApplication>(*args)
}
