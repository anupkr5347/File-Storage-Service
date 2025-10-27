package com.build.distributed.storage.service;

import com.build.distributed.storage.model.FileMetadataRepository;
import com.build.distributed.storage.model.entity.FileMetadata;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
public class GCPFileService implements FileService {
    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final FileMetadataRepository repository;

    private final String bucketName = "file-storage-bucket-gtm-1";

    public GCPFileService(FileMetadataRepository repository) {
        this.repository = repository;
    }

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String objectKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, Objects.requireNonNull(file.getOriginalFilename()));
        Path tmpPath = Files.createTempFile(file.getOriginalFilename(), "");
        file.transferTo(tmpPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(tmpPath));
        Files.delete(tmpPath);

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFileType(file.getContentType());
        metadata.setSize(file.getSize());
        metadata.setObjectKey(objectKey);
        repository.save(metadata);

        return objectKey;
    }

    @Override
    public InputStream downloadFile(String objectKey) throws Exception {
        FileMetadata metadata = repository.findByObjectKey(objectKey);
        String fileName = metadata != null ? metadata.getFileName() : null;

        if (fileName == null) {
            throw new Exception("Could not find file metadata for object key: " + objectKey);
        }

        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        return blob == null ? null : Channels.newInputStream(blob.reader());
    }

    @Override
    public void delete(String objectKey) throws Exception {
        FileMetadata metadata = repository.findByObjectKey(objectKey);
        String fileName = metadata != null ? metadata.getFileName() : null;

        if (fileName == null) {
            throw new Exception("Could not find file metadata for object key: " + objectKey);
        }
        storage.delete(BlobId.of(bucketName, fileName));
        repository.deleteByObjectKey(objectKey);
        System.out.println("File " + objectKey + " deleted.");
    }
}
