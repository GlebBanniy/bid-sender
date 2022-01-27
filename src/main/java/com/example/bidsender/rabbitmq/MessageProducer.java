package com.example.bidsender.rabbitmq;

import com.example.bidsender.dto.BidDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(Source.class)
@Component("originalProducer")
public class MessageProducer implements Producer {

    private final Source mySource;

    @Autowired
    public MessageProducer(Source mySource) {
        this.mySource = mySource;
    }

    @Override
    public BidDto publishMessage(BidDto bidDto){
        mySource.output().send(MessageBuilder.withPayload(bidDto).build());
        return bidDto;
    }

    public Source getMySource() {
        return mySource;
    }
}
