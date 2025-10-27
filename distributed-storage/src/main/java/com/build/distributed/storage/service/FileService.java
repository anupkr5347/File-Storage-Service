package com.build.distributed.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
     String uploadFile(MultipartFile file) throws Exception;
     InputStream downloadFile(String objectName) throws Exception;
     void delete(String objectKey) throws Exception;
}
