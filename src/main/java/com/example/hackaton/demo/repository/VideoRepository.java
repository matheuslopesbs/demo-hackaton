package com.example.hackaton.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hackaton.demo.model.Video;
import com.example.hackaton.demo.model.VideoStatus;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByStatus(VideoStatus status);

    Optional<Video> findByFilename(String filename);
}

