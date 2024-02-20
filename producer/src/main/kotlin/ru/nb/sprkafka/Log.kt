package ru.nb.sprkafka

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Log {
	protected val log: Logger = LoggerFactory.getLogger(javaClass.enclosingClass)
}
