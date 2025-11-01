package com.build.distributed.storage.service;

import com.build.distributed.storage.model.FileMetadataRepository;
import com.build.distributed.storage.model.entity.FileMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Service
public class S3FileService implements FileService{
    private final S3Client s3Client;
    private final FileMetadataRepository repository;


    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3FileService(S3Client s3Client, FileMetadataRepository repository) {
        this.s3Client = s3Client;
        this.repository = repository;
    }

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String key  = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(file.getContentType());
        metadata.setSize(file.getSize());
        metadata.setObjectKey(key);
        repository.save(metadata);

        return key;
    }

    @Override
    public InputStream downloadFile(String objectKey) throws Exception {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build());
    }

    @Override
    public void delete(String objectKey) throws Exception {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build());


    }
}
