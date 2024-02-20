package ru.nb.kafka.cons.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.JacksonUtils
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor
import ru.nb.kafka.cons.service.StringValueConsumer
import ru.nb.kafka.cons.service.StringValueConsumerLogger
import ru.nb.kafka.core.Log
import ru.nb.kafka.core.StringValue

@Configuration
class ConsumerConfig(
	@Value("\${kafka.topic}") val topicName: String,
	@Value("\${kafka.bootstrap-servers}") val bootstrapServers: String,
	@Value("\${kafka.consumer.client-id}") val consumerClientId: String,
	@Value("\${kafka.consumer.group-id}") val consumerGroupId: String,
) {

	@Bean
	fun objectMapper(): ObjectMapper {
		return JacksonUtils.enhancedObjectMapper()
	}

	@Bean
	fun consumerFactory(
		mapper: ObjectMapper
	): ConsumerFactory<String, StringValue> {
		val props = mutableMapOf<String, Any>()
		props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
		props[ConsumerConfig.CLIENT_ID_CONFIG] = consumerClientId
		props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
		props[ConsumerConfig.GROUP_ID_CONFIG] = consumerGroupId

		props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
		props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
		props[TYPE_MAPPINGS] = "ru.nb.kafka.core.StringValue:ru.nb.kafka.core.StringValue"
		props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 3
		props[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = 3000

		val kafkaConsumerFactory = DefaultKafkaConsumerFactory<String, StringValue>(props).apply {
			setValueDeserializer(JsonDeserializer(mapper))
		}
		return kafkaConsumerFactory
//		return DefaultKafkaConsumerFactory(props)
	}

	@Bean(KAFKA_LISTENER_CONTAINER_FACTORY)
	fun listenerContainerFactory(
		consumerFactory: ConsumerFactory<String, StringValue>
	): ConcurrentKafkaListenerContainerFactory<String, StringValue> {
//	): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, StringValue>> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, StringValue>().also {
			it.consumerFactory = consumerFactory
			it.isBatchListener = true
			it.setConcurrency(1)
			it.containerProperties.idleBetweenPolls = 1000
			it.containerProperties.pollTimeout = 1000
		}

		val executor = SimpleAsyncTaskExecutor("consumer-")
		executor.concurrencyLimit = 10
		val listenerTaskExecutor = ConcurrentTaskExecutor(executor)
		factory.containerProperties.listenerTaskExecutor = listenerTaskExecutor
		return factory
	}

	@Bean
	fun topic(): NewTopic {
		return TopicBuilder.name(topicName).partitions(1).replicas(1).build()
	}

	@Bean
	fun stringValueConsumerLogger(): StringValueConsumer {
		return StringValueConsumerLogger()
	}

	@Bean
	fun stringValueConsumer(stringValueConsumer: StringValueConsumer): KafkaClient {
		return KafkaClient(stringValueConsumer)
	}

	class KafkaClient(private val stringValueConsumer: StringValueConsumer) {

		@KafkaListener(
			topics = ["\${kafka.topic}"],
			containerFactory = KAFKA_LISTENER_CONTAINER_FACTORY,
		)
		fun listen(@Payload values: List<StringValue>) {
			log.info("values, values.size: {}", values.size)
			stringValueConsumer.accept(values)
		}
	}

	companion object : Log()
}

//@Service
//class ConsumerKafka(
//	private val stringValueConsumer: StringValueConsumer
//) {
//
//	@KafkaListener(
//		topics = ["\${kafka.topic}"],
//		containerFactory = KAFKA_LISTENER_CONTAINER_FACTORY,
//	)
//	fun listen(@Payload values: List<StringValue>) {
//		log.error("values, values.size: {}", values.size)
//		stringValueConsumer.accept(values)
//	}
//
////	fun listen(@Payload values: List<StringValue>) {
////		log.info("values, values.size: {}", values.size)
////		for (stringValue in values) {
////			log.info("log: {}", stringValue)
////		}
////	}
//
//	companion object : Log()
//}

private const val KAFKA_LISTENER_CONTAINER_FACTORY = "listenerContainerFactory"