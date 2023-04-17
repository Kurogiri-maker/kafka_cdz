package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    //private final TopicProducer topicProducer;

    // private final TopicListener topicListener;


    ObjectMapper mapper = new ObjectMapper();


    @PostMapping(value = "/type")
    public String getDocumentType(@RequestBody String document) throws IOException {

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



        String json = mapper.writeValueAsString(metaData);


        //topicProducer.getDocumentType(fileClassification);
        return json;

    }

    @PostMapping(value = "/collect")
    public String collectDocumentData(@RequestBody String document) throws IOException {
        // Decode Base64 string to byte array
        byte[] fileContent = Base64.getDecoder().decode(document);

        // Perform file classification based on fileContent
        String fileClassification = KafkaService.classifyFile(fileContent , KafkaService.getFilterParameters());
        //create a random string of 6 letters
        String randomString = UUID.randomUUID().toString().substring(0, 6);


        return switch (fileClassification) {
            case "Tiers" ->
                    "{\"id\":\"12345\",\"numero\":\"56789\",\"nom\":\"test\",\"siren\":\"56789\",\"refMandat\":\"98765\"}";
            case "Dossier" ->
                    "{\"id\":\"54321\",\"dossier_DC\":\"facture\",\"numero\":\"986532\",\"listSDC\":\"test\",\"n_DPS\":\"3\",\"montant_du_pres\":\"80\"}";
            //"{\"id\":\"%d\",\"dossier_DC\":\"%s\",\"numero\":\"%s\",\"listSDC\":\"%s\",\"n_DPS\":\"%s\",\"montant_du_pres\":\"%s\"}";

            case "Contrat" ->
                    "{\"id\":57,\"num_dossierKPS\":\"nTbsTf\",\"num_CP\":\"dUcyEW\",\"raison_Social\":\"lxbWJf\",\"id_Tiers\":\"SYRGmC\",\"num_DC\":\"WUKJHS\",\"num_SDC\":\"WTwCQv\",\"num_CIR\":\"ZIjrua\",\"num_SIREN\":\"MxcLzb\",\"refCollaborative\":\"baqDMw\",\"code_Produit\":\"tihXoJ\",\"identifiant_de_offre_comm\":\"VbklYe\",\"chef_de_File\":\"PJgQgx\",\"num_OVI\":\"aOMrXc\",\"num_RUM\":\"EihyMI\",\"typeEnergie\":\"QynLxh\",\"produit_Comm\":\"Zrwbqp\",\"produit\":\"lXOkJd\",\"phase\":\"yCmjwk\",\"montant_pret\":84}";
            default -> "unknown document type";
        };

    }







}


// .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties