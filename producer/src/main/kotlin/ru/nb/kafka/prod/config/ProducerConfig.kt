package ru.nb.kafka.prod.config

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
import org.springframework.kafka.support.serializer.JsonSerializer
import ru.nb.kafka.core.StringValue

@Configuration
class ProducerConfig(
	@Value("\${kafka.topic}") val topicName: String,
	@Value("\${kafka.bootstrap-servers}") val bootstrapServers: String,
	@Value("\${kafka.producer.client-id}") val producerClientId: String,
) {

	@Bean
	fun producerFactory(): ProducerFactory<String, StringValue> {
		val props = mutableMapOf<String, Any>()
		props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
		props[ProducerConfig.CLIENT_ID_CONFIG] = producerClientId
		props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
		props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
		return DefaultKafkaProducerFactory(props)
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