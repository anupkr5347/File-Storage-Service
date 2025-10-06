package com.build.distributed.storage.controller;

import com.build.distributed.storage.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String objectKey = fileService.upload(file);
        return ResponseEntity.ok(objectKey);
    }

    @GetMapping("/download/{objectKey}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectKey) throws Exception {
        InputStream fileStream = fileService.download(objectKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectKey + "\"")
                .body(new InputStreamResource(fileStream));
    }

    @DeleteMapping("/{objectKey}")
    public ResponseEntity<Void> deleteFile(@PathVariable String objectKey) throws Exception {
        fileService.delete(objectKey);
        return ResponseEntity.ok().build();
    }
}

