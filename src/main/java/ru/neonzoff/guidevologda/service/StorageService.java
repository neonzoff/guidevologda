package ru.neonzoff.guidevologda.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Tseplyaev Dmitry
 */
public interface StorageService {
    String uploadFile(MultipartFile file);

    String uploadFile(File file);

    byte[] downloadFile(String fileName);

    String deleteFile(String fileName);
}
