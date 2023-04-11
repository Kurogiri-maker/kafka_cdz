package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final TopicProducer topicProducer;

    private final TopicListener topicListener;

    @PostMapping(value = "/type")
    public void getDocumentType(@RequestBody String document) throws JsonProcessingException {
        // Decode Base64 string to byte array
        byte[] fileContent = Base64.getDecoder().decode(document);

        // Perform file classification based on fileContent
        String fileClassification = classifyFile(fileContent , getFilterParameters());

        topicProducer.getDocumentType(fileClassification);
    }

    @PostMapping(value = "/collect")
    public void collectDocumentData(@RequestBody String document) throws IOException {
        topicProducer.collectDocumentData(document);
    }

    private String classifyFile(byte[] fileContent , List<FilterParameter> filterParameters) {

        String content = new String(fileContent, StandardCharsets.UTF_8);
        // Create a map to store the count of parameters for each category
        Map<String, Integer> categoryCountMap = new HashMap<>();

        for (FilterParameter filterParameter : filterParameters) {
            if (content.contains(filterParameter.getKeyword())) {
                String category = filterParameter.getCategory();

                // If the category is not already in the map, add it with count 1
                // Otherwise, increment the count for the category by 1
                if (!categoryCountMap.containsKey(category)) {
                    categoryCountMap.put(category, 1);
                } else {
                    categoryCountMap.put(category, categoryCountMap.get(category) + 1);
                }
            }
        }
        Map.Entry<String,Integer> highestCategory = null;
        for (Map.Entry<String,Integer> entry : categoryCountMap.entrySet()){
            if(highestCategory == null || entry.getValue() > highestCategory.getValue()){
                highestCategory=entry;
            }
        }
        if(highestCategory == null) {
            return "Unknown";
        }else {
            return highestCategory.getKey();
        }

    }

    private List<FilterParameter> getFilterParameters() {
        List<FilterParameter> filterParameters = new ArrayList<>();
        filterParameters.add(new FilterParameter("siren", "Tiers"));
        filterParameters.add(new FilterParameter("ListSDC", "Dossier"));
        return filterParameters;
    }


    @GetMapping(value = "/collect")
    public String SendCollectedData(){
        return topicListener.getKafkaMessage();
    }

    @GetMapping(value = "/type")
    public String SendTypeOfDocument(){
        return topicListener.getKafkaMessage();
    }

}


// .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties