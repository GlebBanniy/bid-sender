package com.example.bidsender.config;


import com.example.bidsender.rabbitmq.FakeMessageProducer;
import com.example.bidsender.rabbitmq.Producer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MessageProducerConfig {

    @ConditionalOnProperty(value="testrabbit.disabled", havingValue = "true", matchIfMissing = true)
    @Bean
    @Primary
    public Producer fakeMessageProducer(){
        return new FakeMessageProducer();
    }
}
