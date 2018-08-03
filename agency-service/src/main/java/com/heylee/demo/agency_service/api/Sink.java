package com.heylee.demo.agency_service.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String INPUT = "message_input";

    @Input(Sink.INPUT)
    SubscribableChannel input();
}
