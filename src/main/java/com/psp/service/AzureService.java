package com.psp.service;

import com.google.common.io.Files;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

@Service
public class AzureService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    CloudBlobContainer cloudBlobContainer;

    public URI upload(MultipartFile multipartFile){
        URI uri = null;
        CloudBlockBlob blob = null;
        try {
            String uuid = UUID.randomUUID().toString();
            String originalFilename = multipartFile.getOriginalFilename();
            String newFilename = Files.getNameWithoutExtension(originalFilename) + uuid + "." + Files.getFileExtension(originalFilename);
            blob = cloudBlobContainer.getBlockBlobReference(newFilename);
            blob.upload(multipartFile.getInputStream(), -1);
            uri = blob.getUri();
        } catch (URISyntaxException | StorageException | IOException e) {
            e.printStackTrace();
        }
        return uri;
    }
}
