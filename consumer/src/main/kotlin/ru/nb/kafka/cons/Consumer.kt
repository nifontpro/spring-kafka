package ru.nb.kafka.cons

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import ru.nb.kafka.core.Log
import ru.nb.kafka.core.StringValue

@Service
class Consumer {

	@KafkaListener(topics = ["topic"])
	fun list(@Payload value: StringValue) {
		log.info("consumer: $value")
	}

	companion object : Log()

}