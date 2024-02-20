package ru.nb.kafka.prod.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.JacksonUtils
import org.springframework.kafka.support.serializer.JsonSerializer
import ru.nb.kafka.core.StringValue

@Configuration
class ProdConfig(
	@Value("\${kafka.topic}") val topicName: String,
	@Value("\${kafka.bootstrap-servers}") val bootstrapServers: String,
	@Value("\${kafka.producer.client-id}") val producerClientId: String,
) {
	@Bean
	fun objectMapper(): ObjectMapper {
		return JacksonUtils.enhancedObjectMapper()
	}

	@Bean
	fun producerFactory(
		mapper: ObjectMapper
	): ProducerFactory<String, StringValue> {
		val props = mutableMapOf<String, Any>()
		props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
		props[ProducerConfig.CLIENT_ID_CONFIG] = producerClientId
		props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
		props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

		val kafkaProducerFactory = DefaultKafkaProducerFactory<String, StringValue>(props)
		kafkaProducerFactory.valueSerializer = JsonSerializer(mapper)
		return kafkaProducerFactory
	}

	@Bean
	fun kafkaTemplate(
		producerFactory: ProducerFactory<String, StringValue>
	): KafkaTemplate<String, StringValue> {
		return KafkaTemplate(producerFactory)
	}

	@Bean
	fun topic(): NewTopic {
		return TopicBuilder.name(topicName).partitions(1).replicas(1).build()
	}
}