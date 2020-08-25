package com.test;

import java.io.ByteArrayInputStream;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.util.Configuration;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class App 
{
    private static final byte[] CONTENT = "Hello World!".getBytes();

    public static void main( String[] args )
    {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG");

        String connectionString = System.getenv("STORAGE_CONNECTION_STRING");
        if (connectionString == null || connectionString.length() == 0) {
            System.out.println("Environment variable STORAGE_CONNECTION_STRING must be set");
            System.exit(1);
        }

        BlobServiceClient serviceClient = new BlobServiceClientBuilder().connectionString(connectionString)
                .httpLogOptions(BlobServiceClientBuilder.getDefaultHttpLogOptions()
                        .setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                .configuration(Configuration.getGlobalConfiguration().put("AZURE_LOG_LEVEL", "1")).buildClient();
        BlobContainerClient containerClient = serviceClient.getBlobContainerClient("test");
        BlobClient blobClient = containerClient.getBlobClient("test");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(CONTENT);
        blobClient.upload(inputStream, CONTENT.length, true);
    }
}
