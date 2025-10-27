package com.build.distributed.storage.controller;

import com.build.distributed.storage.dto.Location;
import com.build.distributed.storage.service.GCPFileService;
import com.build.distributed.storage.service.MiniIoFileService;
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

    public FileController(MiniIoFileService miniIoFileService, GCPFileService gcpFileService) {
        this.miniIoFileService = miniIoFileService;
        this.gcpFileService = gcpFileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader Location location) throws Exception {
        String objectKey = Location.MINIO.equals(location) ? miniIoFileService.uploadFile(file) :
                gcpFileService.uploadFile(file);
        return ResponseEntity.ok(objectKey);
    }

    @GetMapping("/download/{objectKey}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectKey, @RequestHeader Location location) throws Exception {
        InputStream fileStream = Location.MINIO.equals(location) ? miniIoFileService.downloadFile(objectKey) :
                gcpFileService.downloadFile(objectKey);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectKey + "\"")
                .body(new InputStreamResource(fileStream));
    }

    @DeleteMapping("/{objectKey}")
    public ResponseEntity<Void> deleteFile(@PathVariable String objectKey, @RequestHeader Location location) throws Exception {
        if (Location.MINIO.equals(location)) {
            miniIoFileService.delete(objectKey);
        } else {
            gcpFileService.delete(objectKey);
        }

        return ResponseEntity.ok().build();
    }
}

