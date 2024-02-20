package ru.nb.kafka.prod.ctrl
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.kafka.core.KafkaTemplate
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RestController
//import ru.nb.kafka.core.StringValue
//import kotlin.random.Random
//
//@RestController
//class Ctrl(
//	@Value("\${kafka.topic}") val topicName: String,
//	private val kafkaTemplate: KafkaTemplate<String, StringValue>
//) {
//
//	@GetMapping("/send")
//	fun send() {
//		kafkaTemplate.send(
//			topicName,
//			StringValue(id = Random.nextLong(), value = Random.nextInt().toString())
//		)
//	}
//
//}