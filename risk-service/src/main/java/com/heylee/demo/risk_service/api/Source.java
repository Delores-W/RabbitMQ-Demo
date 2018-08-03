package com.heylee.demo.risk_service.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {

    String OUTPUT = "message_output";

    @Output(Source.OUTPUT)
    MessageChannel output();
}
