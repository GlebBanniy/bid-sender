package com.example.bidsender.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

import static com.example.bidsender.dateservice.DateSetter.setDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CroneJobControllerTest extends BaseControllerTest{

    @Value("${file.testZipFile}")
    private String testZipFile;

    @Value("${name}")
    private String name;

    @Value("${croneJobUrl}")
    private String croneJobUrl;

    @BeforeEach
    void setUp() {
    }

    @Test
    void doCroneJob() throws Exception {
        var date = setDate(21);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            mockMvc.perform(post(croneJobUrl).param(name, testZipFile))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Process file completed"));
        }
    }

    @Test
    void willThrowZipReadingExc() throws Exception {
        mockMvc.perform(post(croneJobUrl).param(name, "BadZip.zip"))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string("Error reading zip file"));
    }

    @Test
    void willThrowFileReadingExc() throws Exception {
        var date = setDate(25);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            mockMvc.perform(post(croneJobUrl).param(name, testZipFile))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().string("File processing error, invalid data, not found end of line ';'"));
        }
    }

    @Test
    void willThrowFileProcessingExc() throws Exception {
        var date = setDate(23);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            mockMvc.perform(post(croneJobUrl).param(name, testZipFile))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().string("File processing error, invalid data"));
        }
    }

    @Test
    void willThrowWhenRequiredFileNotFoundExc() throws Exception {
        var date = setDate(10);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            mockMvc.perform(post(croneJobUrl).param(name, testZipFile))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().string("No file with today's date in archive"));
        }
    }
}
