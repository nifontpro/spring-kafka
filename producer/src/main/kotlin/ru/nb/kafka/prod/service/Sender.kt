package ru.nb.kafka.prod.service

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.nb.kafka.core.Log
import ru.nb.kafka.core.StringValue

@Service
class Sender {

	@Bean
	fun dataSender(
		topic: NewTopic,
		kafkaTemplate: KafkaTemplate<String, StringValue>
	): DataSender {
		return DataSenderKafka(
			topic.name(),
			kafkaTemplate
		) { stringValue -> log.info("asked, value: {}", stringValue) }
	}

	@Bean
	fun stringValueSource(dataSender: DataSender): StringValueSource {
		return StringValueSource(dataSender)
	}

	companion object : Log()

}