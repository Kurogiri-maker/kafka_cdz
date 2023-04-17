package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicProducer {

    @Value("${topic.name.producer}")
    private String topicName;

    @Autowired
    private NewTopic typageTopic;

    @Autowired
    private NewTopic collecteTopic;



    // Serialize the document object to JSON string
    ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void getDocumentType(String document) throws IOException {

        // Decode Base64 string to byte array
        byte[] fileContent = Base64.getDecoder().decode(document);

        // Perform file classification based on fileContent
        String fileClassification = KafkaService.classifyFile(fileContent , KafkaService.getFilterParameters());

        Map<String,String> metadataStringMap = KafkaService.extractMetadataFromPdf(fileContent);

        Map<String,String> metaData = new LinkedHashMap<>();
        metaData.put("Type",fileClassification);

        for (Map.Entry<String, String> entry : metadataStringMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            log.info(key +" : "+ value);
            metaData.put(key, value);
        }

        String json = objectMapper.writeValueAsString(metaData);
        log.info("From demo to pfebackend : "+json);

        kafkaTemplate.send(topicName, json);
    }

    public void collectDocumentData(String documentJson) {
        log.info("Payload : {}", documentJson);
        String jsonString = "{\"id\":\"12345\",\"siren\":\"56789\",\"refMandat\":\"98765\",\"attribute1\":\"value1\",\"attribute2\":\"value2\",\"attribute3\":\"value3\"}";
        log.info("Payload : {}", jsonString);
        kafkaTemplate.send(collecteTopic.name(), jsonString);
    }

}