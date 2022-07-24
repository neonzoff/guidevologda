package ru.neonzoff.guidevologda.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static ru.neonzoff.guidevologda.utils.Converter.convertMultipartFileToFile;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class StorageServiceImpl implements StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;


    public StorageServiceImpl(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    @Transactional
    public String uploadFile(MultipartFile file) {
        File convertedFile = convertMultipartFileToFile(file);
        s3Client.putObject(
                new PutObjectRequest(bucketName, file.getOriginalFilename(), convertedFile)
        );
        convertedFile.delete();

        return "File " + file.getOriginalFilename() + " was uploaded";
    }

    @Override
    public String uploadFile(File file) {
        s3Client.putObject(
                new PutObjectRequest(bucketName, file.getName(), file)
        );
        return "File " + file.getName() + " was uploaded";
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = object.getObjectContent();
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " was removed";
    }

}
