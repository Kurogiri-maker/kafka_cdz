package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicProducer {

    @Value("${topic.name.producer}")
    private String topicName;

    // Serialize the document object to JSON string
    ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(Document document) throws JsonProcessingException {
        String documentJson = objectMapper.writeValueAsString(document);
        log.info("Payload : {}", documentJson);
        kafkaTemplate.send(topicName, documentJson);
    }

}
