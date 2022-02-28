package com.example.bidsender.rabbitmq;

import com.example.bidsender.dto.BidDto;
import org.springframework.cloud.stream.messaging.Source;

public interface Producer {
    BidDto publishMessage(BidDto bidDto);
    Source getMySource();
}
