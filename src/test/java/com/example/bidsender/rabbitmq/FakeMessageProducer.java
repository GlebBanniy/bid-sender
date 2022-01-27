package com.example.bidsender.rabbitmq;

import com.example.bidsender.dto.BidDto;
import org.springframework.cloud.stream.messaging.Source;

public class FakeMessageProducer implements Producer {

    @Override
    public BidDto publishMessage(BidDto bidDto) {
        return bidDto;
    }

    @Override
    public Source getMySource() {
        return null;
    }
}
