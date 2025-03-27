package com.example.hackaton.demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaVideoProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName = "video-process-topic";  // Tópico Kafka

    public KafkaVideoProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVideoMessage(String videoId) {
        kafkaTemplate.send(topicName, videoId);
        System.out.println("Mensagem enviada ao Kafka para processar o vídeo: " + videoId);
    }
}

