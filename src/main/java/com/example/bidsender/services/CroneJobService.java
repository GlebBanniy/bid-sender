package com.example.bidsender.services;

import com.example.bidsender.exceptions.FileProcessingException;
import com.example.bidsender.exceptions.JobProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Service
public class CroneJobService {

    private final BankService bankService;

    @Autowired
    public CroneJobService(BankService bankService) {
        this.bankService = bankService;
    }

    private InputStreamReader getRequiredFile(String fileName) throws FileProcessingException {
        try {
            var zipFile = ZipFileFactory.makeZipFile(fileName);
            var entries = zipFile.entries();
            String name;
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                name = entry.getName();
                if (name.equals(currentDate())) {
                    return new InputStreamReader(zipFile.getInputStream(entry));
                }
                log.info("Skipped file: " + name);
            }
            throw new FileProcessingException("No file with today's date in archive");
        } catch (IOException e) {
            throw new FileProcessingException("Error reading zip file");
        }
    }

    private String currentDate() {
        var today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        return "currencies_" + today + ".txt";
    }

    public void startJob(String fileName) throws JobProcessingException {
        try {
            readFile(fileName);
        } catch (FileProcessingException e) {
            throw new JobProcessingException(e.getMessage());
        }
    }

    private void readFile(String fileName) throws FileProcessingException {
        try (var in = BufferedReaderFactory.makeBufferedReader(getRequiredFile(fileName))) {
            int currentSymbol;
            boolean isEndOfLine = false;
            String s = "";
            while ((currentSymbol = in.read()) != -1) {
                s += (char) currentSymbol;
                if ((char) currentSymbol == ';') {
                    isEndOfLine = true;
                    bankService.parseLine(s);
                    s = "";
                }
            }
            if (!isEndOfLine)
                throw new FileProcessingException("File processing error, invalid data, not found end of line ';'");
        } catch (IOException e) {
            throw new FileProcessingException("Error reading file");
        }
        bankService.sendData();
    }
}
