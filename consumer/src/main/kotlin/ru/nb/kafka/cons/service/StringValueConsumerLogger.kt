package ru.nb.kafka.cons.service

import ru.nb.kafka.core.Log
import ru.nb.kafka.core.StringValue

class StringValueConsumerLogger : StringValueConsumer {

	override fun accept(value: List<StringValue>) {
		for (stringValue in value) {
			log.info("log:{}", stringValue)
		}
	}

	companion object : Log()
}