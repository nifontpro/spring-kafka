package ru.nb.sprkafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import ru.nb.sprkafka.prod.StringValue

@Service
class Consumer {

	@KafkaListener(topics = ["topic"])
	fun list(@Payload value: StringValue) {
		log.info("consumer: $value")
	}

	companion object : Log()

}