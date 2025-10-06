package com.build.distributed.storage.service;

import com.build.distributed.storage.model.FileMetadataRepository;
import com.build.distributed.storage.model.entity.FileMetadata;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileService {

    private final MinioClient minioClient;
    private final FileMetadataRepository repository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public FileService(MinioClient minioClient, FileMetadataRepository repository) {
        this.minioClient = minioClient;
        this.repository = repository;
    }

    public String upload(MultipartFile file) throws Exception {
        String objectKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Upload file to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // Save metadata
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(file.getContentType());
        metadata.setSize(file.getSize());
        metadata.setObjectKey(objectKey);
        repository.save(metadata);

        return objectKey;
    }

    public InputStream download(String objectKey) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .build()
        );
    }

    public void delete(String objectKey) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .build()
        );
        repository.deleteByObjectKey(objectKey);
    }
}

