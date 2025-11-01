package com.build.distributed.storage.controller;

import com.build.distributed.storage.dto.Location;
import com.build.distributed.storage.service.GCPFileService;
import com.build.distributed.storage.service.MiniIoFileService;
import com.build.distributed.storage.service.S3FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@RestController
@RequestMapping("/files")
public class FileController {

    private final MiniIoFileService miniIoFileService;
    private final GCPFileService gcpFileService;
    private final S3FileService s3FileService;

    public FileController(MiniIoFileService miniIoFileService, GCPFileService gcpFileService, S3FileService s3FileService) {
        this.miniIoFileService = miniIoFileService;
        this.gcpFileService = gcpFileService;
        this.s3FileService = s3FileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader Location location) throws Exception {
        String objectKey = null;
        if (Location.MINIO.equals(location)) {
            objectKey = miniIoFileService.uploadFile(file);
        } else if (Location.GCP.equals(location)) {
            objectKey = gcpFileService.uploadFile(file);
        } else if (Location.S3.equals(location)) {
            objectKey =  s3FileService.uploadFile(file);
        }

        return ResponseEntity.ok(objectKey);
    }

    @GetMapping("/download/{objectKey}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectKey, @RequestHeader Location location) throws Exception {
        InputStream fileStream = null;
        if (Location.MINIO.equals(location)) {
            fileStream = miniIoFileService.downloadFile(objectKey);
        } else if (Location.GCP.equals(location)) {
            fileStream = gcpFileService.downloadFile(objectKey);
        } else if (Location.S3.equals(location)) {
            fileStream = s3FileService.downloadFile(objectKey);
        }
        assert fileStream != null;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectKey + "\"")
                .body(new InputStreamResource(fileStream));
    }

    @DeleteMapping("/{objectKey}")
    public ResponseEntity<Void> deleteFile(@PathVariable String objectKey, @RequestHeader Location location) throws Exception {
        if (Location.MINIO.equals(location)) {
            miniIoFileService.delete(objectKey);
        } else if (Location.GCP.equals(location)) {
            gcpFileService.delete(objectKey);
        } else if (Location.S3.equals(location)) {
            s3FileService.delete(objectKey);
        }

        return ResponseEntity.ok().build();
    }
}

