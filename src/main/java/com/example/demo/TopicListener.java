package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicListener {

    @Value("${topic.name.consumer}")
    private String topicName;

    @Autowired
    private NewTopic typageTopic;

    @Autowired
    private NewTopic collecteTopic;

    private String kafkaMessage;

    List<String> tiersAttributes = List.of("id","name","siren","refMandat");

    List<String> contratAttributes = List.of("id","name","siren","refMandat");

    List<String> DossierAttributes = List.of("id","name","siren","refMandat");




    private ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "#{@typageTopic.name}",groupId = "group_id")
    public void processDocumentForTypage(@Payload String payload) throws JsonProcessingException {

        //Document tiers = objectMapper.readValue(payload, Document.class);

        List<String> attributesList = new ArrayList<>();

        // Parse the JSON string into a JsonNode object
        JsonNode jsonNode = objectMapper.readTree(payload);

        // Extract keys from the JsonNode object
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            attributesList.add(fieldName);
        }

        if (attributesList.containsAll(tiersAttributes)) {
            System.out.println("This is Tiers");
        } else {
            System.out.println("This is not a Tiers");
        }

    }


    @KafkaListener(topics = "#{@collecteTopic.name}",groupId = "group_id")
    public void processDocumentForCollect(@Payload String payload) throws JsonProcessingException {


        List<String> attributesList = new ArrayList<>();

        // Parse the JSON string into a JsonNode object
        JsonNode jsonNode = objectMapper.readTree(payload);

        // Extract keys from the JsonNode object
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            attributesList.add(fieldName);
        }

        for (String attribute : attributesList) {
            log.info(attribute);
        }

        kafkaMessage = payload;

        /*
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

        log.info("Topic : " + collecteTopic.name());
        log.info("List of Attributes:");
        for (String attribute : attributesList) {
            log.info(attribute);
        }
         */

    }

    public String getKafkaMessage(){
        return this.kafkaMessage;
    }




}