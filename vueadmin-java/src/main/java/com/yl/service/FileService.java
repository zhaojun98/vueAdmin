package com.yl.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String upload(MultipartFile file) throws IOException;

    String upload(List<MultipartFile> files) throws Exception;

    boolean deleteFile(String filename);

    void deleteFiles(List<String> fileNames);
}
