package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicListener {

    @Value("${topic.name.consumer}")
    private String topicName;

    /*
    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public void consume(ConsumerRecord<String, String> payload){log.info("Topic: {}", topicName);
        log.info("key: {}", payload.key());
        log.info("Headers: {}", payload.headers());
        log.info("Partion: {}", payload.partition());
        log.info("Order: {}", payload.value());

    }

     */

    private ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "${topic.name.consumer}",groupId = "group_id")
    public void processDocument(@Payload String payload) throws JsonProcessingException {

        Document tiers = objectMapper.readValue(payload, Document.class);

        List<String> attributesList = new ArrayList<>();
        attributesList.add("Id: " + tiers.getId());
        attributesList.add("Name: " + tiers.getName());
        attributesList.add("Siren: " + tiers.getSiren());
        attributesList.add("Ref Mandat: " + tiers.getRefMandat());
        for (Map.Entry<String, String> entry : tiers.getAdditionalAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            attributesList.add(key + ": " + value);
        }

        log.info("List of Attributes:");
        for (String attribute : attributesList) {
            log.info(attribute);
        }




    }


}