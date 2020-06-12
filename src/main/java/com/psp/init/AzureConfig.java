package com.psp.init;

import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.microsoft.azure.storage.CloudStorageAccount;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class AzureConfig {

    @Autowired
    private Environment environment;

    @Bean
    public CloudBlobClient cloudBlobClient() throws URISyntaxException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(environment.getProperty("azure.storage.EndpointsProtocol")
                + environment.getProperty("azure.storage.AccountName")
                + environment.getProperty("azure.storage.AccountKey")
                + environment.getProperty("azure.storage.EndpointSuffix"));


        return storageAccount.createCloudBlobClient();
    }

    @Bean
    public CloudBlobContainer testBlobContainer() throws URISyntaxException, StorageException, InvalidKeyException {
        CloudBlobContainer container =  cloudBlobClient().getContainerReference(environment.getProperty("azure.storage.container.name"));
        container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
                new OperationContext());
        return container;
    }

}