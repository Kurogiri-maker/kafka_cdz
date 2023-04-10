package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final TopicProducer topicProducer;

    private final TopicListener topicListener;

    @PostMapping(value = "/type")
    public void getDocumentType(@RequestBody String document) throws JsonProcessingException {
        topicProducer.getDocumentType(document);
    }

    @PostMapping(value = "/collect")
    public void collectDocumentData(@RequestBody String document) throws JsonProcessingException {
        topicProducer.collectDocumentData(document);
    }

    @GetMapping(value = "/collect")
    public String SendCollectedData(){
        return topicListener.getKafkaMessage();
    }
}


// .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties