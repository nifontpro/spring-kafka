package ru.nb.kafka.prod.service

import ru.nb.kafka.core.Log
import ru.nb.kafka.core.StringValue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class StringValueSource(private val dataSender: DataSender) : ValueSource {
	private val nextValue = AtomicLong(1)

	override fun generate() {
		val executor = Executors.newScheduledThreadPool(1)
		executor.scheduleAtFixedRate({ dataSender.send(makeValue()) }, 0, 1, TimeUnit.SECONDS)
		log.info("generation started")
	}

	private fun makeValue(): StringValue {
		val id = nextValue.getAndIncrement()
		return StringValue(id, "stVal: $id")
	}

	companion object: Log()
}
