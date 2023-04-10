package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final TopicProducer topicProducer;

    @PostMapping(value = "/send")
    public void send(@RequestBody Document document) throws JsonProcessingException {
        topicProducer.send(document);
    }
}


// .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties