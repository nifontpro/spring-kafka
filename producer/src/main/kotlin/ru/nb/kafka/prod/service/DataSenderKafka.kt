package ru.nb.kafka.prod.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import ru.nb.kafka.core.Log
import ru.nb.kafka.core.StringValue

interface DataSender {
	fun send(value: StringValue)
}

class DataSenderKafka(
	private val topic: String,
	private val template: KafkaTemplate<String, StringValue>,
	private val sendAsk: (StringValue) -> Unit
) : DataSender {

	override fun send(value: StringValue) {
		try {
			log.info("value: {}", value)

			/**
			 * Отправка асинхронная, но если
			 * ожидание подключения и др., то метод блокируется
			 */
			template.send(topic, value)
				.whenComplete { result: SendResult<String, StringValue>, ex: Throwable? ->
					if (ex == null) {
						log.info(
							"message id:{} was sent, offset:{}",
							value.id,
							result.recordMetadata.offset()
						)
						sendAsk(value)
					} else {
						log.error("message id:{} was not sent", value.id, ex)
					}
				}
		} catch (ex: Exception) {
			log.error("send error, value:{}", value, ex)
		}
	}

	companion object : Log()
}