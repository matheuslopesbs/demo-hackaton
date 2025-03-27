package com.example.hackaton.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.hackaton.demo.service.VideoProcessingService;

@Service
public class KafkaVideoConsumer {

    private final VideoProcessingService videoProcessingService;

    public KafkaVideoConsumer(VideoProcessingService videoProcessingService) {
        this.videoProcessingService = videoProcessingService;
    }

    @KafkaListener(topics = "video-process-topic", groupId = "video-processing-group")
    public void consumeVideoMessage(String videoId) {
        System.out.println("Mensagem recebida para processar o vídeo: " + videoId);

        // Executa o processamento do vídeo
        String videoPath = "/videos/" + videoId + ".mp4";
        videoProcessingService.processVideo(videoPath, videoId);
    }
}

