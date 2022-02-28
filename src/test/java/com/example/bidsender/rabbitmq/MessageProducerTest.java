package com.example.bidsender.rabbitmq;

import com.example.bidsender.base.BaseTest;
import com.example.bidsender.dto.BidDto;
import com.example.bidsender.exceptions.JobProcessingException;
import com.example.bidsender.services.CroneJobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.cloud.stream.test.binder.MessageCollectorAutoConfiguration;
import org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.bidsender.dateservice.DateSetter.setDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PropertySource("classpath:application-rabbit-test.properties")
@ImportAutoConfiguration({TestSupportBinderAutoConfiguration.class, MessageCollectorAutoConfiguration.class})
class MessageProducerTest extends BaseTest {

    @Value("${file.testZipFile}")
    private String testZipFile;

    private BidDto bidDto;

    @Autowired
    private CroneJobService croneJobService;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private Producer messageProducer;

    @BeforeEach
    void setUp() {
        bidDto = new BidDto(
                "Spb",
                "USDRUB",
                72.13,
                true);
    }

    @Test
    void tryPublishMessage() throws JobProcessingException {
        var date = setDate(21);
        try(var mock = Mockito.mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(date);
            croneJobService.startJob(testZipFile);
            Gson g = new Gson();
            var result = g.fromJson(Objects.requireNonNull(messageCollector.forChannel(
                            messageProducer.getMySource().output()).poll()).getPayload().toString(),
                    BidDto.class
            );
            assertEquals(bidDto, result);
        }
    }
}
