package com.example.hackaton.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Service
public class VideoProcessingService {

    public void processVideo(String videoPath, String videoId) {
        try {
            // Configuração do FFmpeg e FFprobe
            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");

            // Processar vídeo e gerar imagens
            String outputFolder = "/videos/output/" + videoId;
            File dir = new File(outputFolder);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    System.err.println("Falha ao criar diretório de saída: " + outputFolder);
                    throw new IOException("Não foi possível criar o diretório de saída.");
                }
            }
            
            // Exemplo de extração de frames (você pode ajustar o intervalo e tamanho)
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(videoPath)
                    .addOutput(outputFolder + "/frame_%04d.jpg")
                    .setFrames(10)
                    .setVideoFilter("fps=1")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

            // Compactar imagens em ZIP
            String zipFilePath = "/videos/output/" + videoId + ".zip";
            zipDirectory(outputFolder, zipFilePath);

            System.out.println("Processamento concluído e arquivo zip gerado: " + zipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zipDirectory(String folderPath, String zipPath) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(new java.io.FileOutputStream(zipPath))) {
            File folder = new File(folderPath);
            for (File file : folder.listFiles()) {
                try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
            }
        }
    }
}
