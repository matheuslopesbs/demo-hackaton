package com.example.hackaton.demo.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.hackaton.demo.kafka.KafkaVideoProducer;
import com.example.hackaton.demo.model.Video;
import com.example.hackaton.demo.repository.VideoRepository;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final KafkaVideoProducer kafkaVideoProducer;
    private final VideoRepository videoRepository;

    public VideoController(KafkaVideoProducer kafkaVideoProducer, VideoRepository videoRepository) {
        this.kafkaVideoProducer = kafkaVideoProducer;
        this.videoRepository = videoRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("video") MultipartFile file) throws Exception {
        try {
            // Gera um ID único para o vídeo
            String videoId = UUID.randomUUID().toString();

            // Define o caminho do arquivo
            String videoPath = "/videos/" + videoId + ".mp4";

            // Cria o diretório se não existir
            java.io.File videoDirectory = new java.io.File("/videos");
            if (!videoDirectory.exists()) {
                videoDirectory.mkdirs();  // Cria o diretório, se não existir
            }

            // Salva o arquivo de vídeo em um diretório local
            file.transferTo(new java.io.File(videoPath));

            // Envia a mensagem para o Kafka para processar o vídeo
            kafkaVideoProducer.sendVideoMessage(videoId);

            return ResponseEntity.ok("Vídeo recebido e processamento iniciado: " + videoId);
        } catch (Exception e) {
            // Lança a exceção para ser tratada no handler
            throw new RuntimeException("Erro ao salvar o vídeo: " + e.getMessage(), e);
        }
    }

    @GetMapping("/status/{videoId}")
    public ResponseEntity<String> getVideoStatus(@PathVariable String videoId) {
        // Busca o vídeo no banco de dados pelo filename
        Optional<Video> videoOptional = videoRepository.findByFilename(videoId);
        
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            return ResponseEntity.ok("Status do vídeo " + videoId + ": " + video.getStatus());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vídeo não encontrado");
        }
    }
}

