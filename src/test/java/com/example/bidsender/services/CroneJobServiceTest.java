package com.example.bidsender.services;

import com.example.bidsender.exceptions.JobProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import static com.example.bidsender.dateservice.DateSetter.setDate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class CroneJobServiceTest extends BaseServiceTest{

    @Value("${file.testZipFile}")
    private String testZipFile;

    @Value("${file.testInvalidZipFile}")
    private String testInvalidZipFile;

    private Bank bank1;
    private Bank bank2;

    @Autowired
    private CroneJobService croneJobService;

    @BeforeEach
    void setUp() {
        bank1 = new Bank(
                "Sber",
                "USDRUB",
                72.13,
                1642151485
        );
        bank2 = new Bank(
                "Spb",
                "USDRUB",
                72.13,
                1642151486
        );
    }

    @Test
    void tryReadFile() throws JobProcessingException {
        var date = setDate(21);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            croneJobService.startJob(testZipFile);
            var field = croneJobService.getClass().getDeclaredField("bankService");
            field.setAccessible(true);
            var bankServiceClass = (BankService) field.get(croneJobService);
            var banksField = bankServiceClass.getClass().getDeclaredField("banks");
            banksField.setAccessible(true);
            var banksList = (ArrayList<Bank>) banksField.get(bankServiceClass);
            assertEquals(2, banksList.size());
            assertEquals(bank1, banksList.get(0));
            assertEquals(bank2, banksList.get(1));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    void willThrowWhenFileNotFound() throws IOException {
        try(var mockZff = Mockito.mockStatic(ZipFileFactory.class)) {
            mockZff.when(() -> ZipFileFactory.makeZipFile(any(String.class))).thenReturn(new ZipFile(testZipFile));
            assertThatThrownBy(() -> croneJobService.startJob(testZipFile))
                    .isInstanceOf(JobProcessingException.class)
                    .hasMessageContaining("No file with today's date in archive");
        }
    }

    @Test
    void willThrowWhenZipReadingError() {
        try(var mockZff = Mockito.mockStatic(ZipFileFactory.class)) {
            mockZff.when(() -> ZipFileFactory.makeZipFile(any(String.class))).thenThrow(IOException.class);
            assertThatThrownBy(() -> croneJobService.startJob(testZipFile))
                    .isInstanceOf(JobProcessingException.class)
                    .hasMessageContaining("Error reading zip file");
        }
    }

    @Test
    void willThrowWhenFileReadingError() throws IOException {
        var date = setDate(21);
        try(var mock = Mockito.mockStatic(LocalDateTime.class);
            var mockBrf = Mockito.mockStatic(BufferedReaderFactory.class);
            var mockZff = Mockito.mockStatic(ZipFileFactory.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            mockBrf.when(() -> BufferedReaderFactory.makeBufferedReader(any(InputStreamReader.class))).thenThrow(IOException.class);
            mockZff.when(() -> ZipFileFactory.makeZipFile(any(String.class))).thenReturn(new ZipFile(testZipFile));
            assertThatThrownBy(() -> croneJobService.startJob(testZipFile))
                    .isInstanceOf(JobProcessingException.class)
                    .hasMessageContaining("Error reading file");
        }
    }

    @Test
    void willThrowWhenDataIsNotValid() {
        var date = setDate(21);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            assertThatThrownBy(() -> croneJobService.startJob(testInvalidZipFile))
                    .isInstanceOf(JobProcessingException.class)
                    .hasMessageContaining("File processing error, invalid data");
        }
    }

    @Test
    void willThrowWhenNotFoundEndOfLine() {
        var date = setDate(22);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            assertThatThrownBy(() -> croneJobService.startJob(testInvalidZipFile))
                    .isInstanceOf(JobProcessingException.class)
                    .hasMessageContaining("File processing error, invalid data, not found end of line ';'");
        }
    }
}
