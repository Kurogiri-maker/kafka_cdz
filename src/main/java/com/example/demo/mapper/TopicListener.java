package com.example.demo.mapper;


import com.example.demo.dto.AdditionalAttributeDTO;
import com.example.demo.model.AdditionalAttribute;
import com.example.demo.model.Document;
import com.example.demo.repository.AdditionalAttributeRepository;
import com.example.demo.repository.DocumentRepository;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class TopicListener {

    @Autowired
    private AdditionalAttributeRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicListener.class);

    @Autowired
    private AdditionalAttributeMapper mapper;

    @Autowired
    private DocumentRepository documentRepository;



    @Autowired
    private NewTopic enrichmentTopic;

    @Autowired
    private NewTopic newAttributesTopic;


    // Serialize the document object to JSON string
    ObjectMapper objectMapper = new ObjectMapper();

    private KafkaTemplate<String, String> kafkaTemplate;



   /* @KafkaListener(topics = "#{@typageTopic.name}",groupId = "group_id")
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


    }*/



    @KafkaListener(topics = "#{@newAttributesTopic.name}",groupId = "group_id")
    public void getNewAttributes(@Payload String payload) throws JsonProcessingException {

        LOGGER.info("///////////////////////////////////////////////////////////////////////////////\n" +
                " Consumer: New ATTRIBUTES//////////////////////////////////////////////////////////\n" +
                "Payload : "+ payload+"/////////////////////////////////////////////////////////////\n" +
                "//////////////////////////////////////////////////////////\n");

        // Parse the JSON string into a JsonNode object
        JsonNode jsonNode = objectMapper.readTree(payload);
        jsonNode.get("type").asText();

        AdditionalAttributeDTO dto = objectMapper.readValue(
                payload,
                AdditionalAttributeDTO.class
        );

        LOGGER.info("///////////////////////////////////////////////////////////////////////////////\n" +
                " Consumer: DTO//////////////////////////////////////////////////////////\n" +
                "dto : "+ dto+"/////////////////////////////////////////////////////////////\n" +
                "//////////////////////////////////////////////////////////\n");
        AdditionalAttribute attribute = mapper.toEntity(dto);
        this.repository.save(attribute);

    }

    @KafkaListener(topics = "#{@synchronisationTopic.name}",groupId = "group_id")
    public void getNewDocuments(@Payload String payload) throws JsonProcessingException {

        LOGGER.info("///////////////////////////////////////////////////////////////////////////////\n" +
                " Consumer: New Document//////////////////////////////////////////////////////////\n" +
                "Payload : "+ payload+"/////////////////////////////////////////////////////////////\n" +
                "//////////////////////////////////////////////////////////\n");

        //extract the type and the numero of the document from the payload
        String[] document = payload.split(" ");
        String type = document[0];
        String numero = document[1];

        LOGGER.info("///////////////////////////////////////////////////////////////////////////////\n" +
                " Consumer: New Document extract type & numero///////////////////////////\n" +
                "type : "+ type+"/////////////////////////////////////////////////////////////\n" +
                "numero : "+ numero+"/////////////////////////////////////////////////////////////\n" +
                "//////////////////////////////////////////////////////////\n");

        Document document1 = Document.builder()
                .numero(numero)
                .type(type)
                .filePath("File"+numero+".pdf")
                .build();

        documentRepository.save(document1);

        LOGGER.info("///////////////////////////////////////////////////////////////////////////////\n" +
                " Consumer: New Document saved in DB////////////////////////////////////////////////////\n" +
                document1+ "  /////////////////////////////////////////////////////////////\n" +
                "//////////////////////////////////////////////////////////\n");





        //



    }








}

