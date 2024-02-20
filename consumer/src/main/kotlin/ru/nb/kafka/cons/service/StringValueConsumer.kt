package ru.nb.kafka.cons.service

import ru.nb.kafka.core.StringValue

interface StringValueConsumer {
	fun accept(value: List<StringValue>)
}