package com.example.bidsender.controllers;

import com.example.bidsender.services.DownloadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DownloadFileControllerTest extends BaseControllerTest{

    @Value("${file.testZipFile}")
    private String testZipFile;

    @Value("${uploadUrl}")
    private String uploadUrl;

    private MockMultipartFile multipartFile;
    private MockMultipartFile multipartFileNotZip;

    @MockBean
    protected DownloadServiceImpl downloadService;

    @BeforeEach
    void setUp() throws IOException {
        multipartFile = new MockMultipartFile(
                "file",
                "hello.zip",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                Files.readAllBytes(Paths.get(testZipFile))
        );
        multipartFileNotZip = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Spring Framework".getBytes());
    }

    @Test
    void provideUploadInfo() throws Exception {
        mockMvc.perform(get(uploadUrl))
                .andExpect(status().isOk());
    }

    @Test
    void handleFileUpload() throws Exception {
        given(downloadService.readFile(any(MockMultipartFile.class)))
                .willReturn(true);
        mockMvc.perform(multipart(uploadUrl)
                        .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().string("File is upload"));
    }

    @Test
    void willThrowWhenFileNotLoad() throws Exception {
        given(downloadService.readFile(any(MockMultipartFile.class)))
                .willReturn(false);
        mockMvc.perform(multipart(uploadUrl)
                        .file(multipartFile))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("File is not upload"));
    }

    @Test
    void willThrowWhenFileNotZip() throws Exception {
        mockMvc.perform(multipart(uploadUrl)
                        .file(multipartFileNotZip))
                .andExpect(status().isForbidden())
                .andExpect(content().string("File is not ZIP"));
    }
}
