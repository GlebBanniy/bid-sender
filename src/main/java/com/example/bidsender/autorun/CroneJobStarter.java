package com.example.bidsender.autorun;

import com.example.bidsender.exceptions.JobProcessingException;
import com.example.bidsender.services.CroneJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@EnableAsync
public class CroneJobStarter {

    private final CroneJobService croneJobService;

    @Value("${file.data.name}")
    private String fileName;

    @Autowired
    public CroneJobStarter(CroneJobService croneJobService) {
        this.croneJobService = croneJobService;
    }

    @Scheduled(cron = "0 50 23 * * MON-FRI")
    @Async
    public void doJob() throws JobProcessingException {
        croneJobService.startJob(fileName);
    }
}
