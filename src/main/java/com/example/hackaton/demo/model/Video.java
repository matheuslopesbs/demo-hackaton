package com.example.hackaton.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "videos")
public class Video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Enumerated(EnumType.STRING)  // Indica que o enum será salvo como String no banco de dados
    @Column(name = "status", nullable = false)
    private VideoStatus status;

    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;

    @Column(name = "username", nullable = false)  // Renomeado de 'user' para 'username'
    private String username;

    // Construtores
    public Video() {
    }

    public Video(String filename, VideoStatus status, LocalDateTime uploadTime, String username) {
        this.filename = filename;
        this.status = status;
        this.uploadTime = uploadTime;
        this.username = username;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public VideoStatus getStatus() {
        return status;
    }

    public void setStatus(VideoStatus status) {
        this.status = status;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", status=" + status +
                ", uploadTime=" + uploadTime +
                ", username='" + username + '\'' +
                '}';
    }
}

