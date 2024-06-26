package com.example.demo.mapper;

import com.example.demo.model.EnrichmentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private NewTopic enrichmentTopic;

    // Serialize the document object to JSON string
    ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;



    /*public void getDocumentType(String documentJson) throws JsonProcessingException {
        log.info("Payload : {}", documentJson);
        String jsonString = "{\"type\":\"" + documentJson + "\"}";
        kafkaTemplate.send(typageTopic.name(), jsonString);
    }

    public void collectDocumentData(String documentJson) throws JsonProcessingException {
        log.info("Payload : {}", documentJson);
        String jsonString = "{\"id\":\"12345\",\"siren\":\"56789\",\"refMandat\":\"98765\",\"attribute1\":\"value1\",\"attribute2\":\"value2\",\"attribute3\":\"value3\"}";
        log.info("Payload : {}", jsonString);
        kafkaTemplate.send(collecteTopic.name(), jsonString);
    }*/

    public void sendDocument(EnrichmentResponse docProcessed) throws JsonProcessingException {
//        String json = objectMapper.writeValueAsString(docProcessed);
        String json = docProcessed.toString();
        kafkaTemplate.send(enrichmentTopic.name(),json);
    }

}

