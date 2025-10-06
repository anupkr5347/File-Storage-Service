package com.build.distributed.storage.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FileMetadata {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;
    private String fileType;
    private Long size;
    private String objectKey; // Key in MinIO bucket

    private LocalDateTime createdAt = LocalDateTime.now();
}

