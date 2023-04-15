package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    private final TopicProducer topicProducer;

    // private final TopicListener topicListener;

    private String document;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;



    @KafkaListener(topics = "pdf-type", groupId = "group_id")
    public void consume(String message) throws IOException {
        // Decode Base64 string to byte array
        byte[] fileContent = Base64.getDecoder().decode(message);

        // Perform file classification based on fileContent
        String fileClassification = classifyFile(fileContent , getFilterParameters());

        // Produce message to another Kafka topic
        kafkaTemplate.send("pdf-type-result", fileClassification);

        log.info("Message processed and sent to Kafka topic pdf-type-result: {}", fileClassification);
    }




    @PostMapping(value = "/type")
    public String getDocumentType(@RequestBody String document) throws IOException {
        // Decode Base64 string to byte array
        byte[] fileContent = Base64.getDecoder().decode(document);

        // Perform file classification based on fileContent
        String fileClassification = classifyFile(fileContent , getFilterParameters());


        topicProducer.getDocumentType(fileClassification);
        /*
        String result = topicListener.getTypageMessage();
        while(result == null){
            try {
                Thread.sleep(100); // Sleep for 100 milliseconds
            } catch (InterruptedException e) {
                // Handle InterruptedException if necessary
                e.printStackTrace();
            }
            result = topicListener.getTypageMessage();
        }

        log.info(result);
        */
        return "Message Produced";

    }

    @PostMapping(value = "/collect")
    public void collectDocumentData(@RequestBody String document) throws IOException {
        topicProducer.collectDocumentData(document);
    }

    private String extractTextFromPdf(byte[] fileBytes) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(new ByteArrayInputStream(fileBytes));
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(document);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
    private String classifyFile(byte[] fileContent , List<FilterParameter> filterParameters) throws IOException {

        //String content = new String(fileContent, StandardCharsets.UTF_8);
        String content = extractTextFromPdf(fileContent);
        log.info("Content : " +content);
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
        return this.document;
    }

    @PostMapping(value = "/collect/demo")
    public String sendCollectData(@RequestBody String document){
        setDocument(document);
        return document;
    }

    public void setDocument(String val){
        this.document=val;
    }



}


// .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties