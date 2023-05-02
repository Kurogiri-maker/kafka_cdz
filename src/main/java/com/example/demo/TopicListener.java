package com.example.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class TopicListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicListener.class);


    @Value("${topic.name.consumer}")
    private String topicName;

    @Autowired
    private NewTopic typageTopic;

    @Autowired
    private NewTopic collecteTopic;

    @Autowired
    private NewTopic enrichmentTopic;



    private ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "#{@typageTopic.name}",groupId = "group_id")
    public void processDocumentForTypage(@Payload String payload) throws JsonProcessingException {

        // Parse the JSON string into a JsonNode object
        JsonNode jsonNode = objectMapper.readTree(payload);



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


    }

    @KafkaListener(topics = "#{@enrichmentTopic.name}",groupId = "group_id")
    public void getDocumentProcessed(@Payload String payload) throws JsonProcessingException {

        LOGGER.info("///////////////////////////////////////////////////////////////////////////////\n" +
                " Consumer //////////////////////////////////////////////////////////////////////////\n" +
                "///////////////////////////////////////////////////////////////////////////////////\n" +
                "Payload : "+ payload+"///////////////////////////////////////////////////////////////////\n" +
                "/////////////////////////////////////////////////////////\n" +
                "//////////////////////////////////////////////////////////\n");

    }







}

