package com.example.bidsender.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DownloadServiceImplTest extends BaseServiceTest{

    @Value("${file.testZipFile}")
    private String testZipFile;

    @Autowired
    private DownloadServiceImpl downloadService;

    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {
        multipartFile = new MockMultipartFile(
                "file",
                "hello.zip",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                Files.readAllBytes(Paths.get(testZipFile))
        );
    }

    @Test
    void readFile() {
        var result = downloadService.readFile(multipartFile);
        assertTrue(result);
    }
}
