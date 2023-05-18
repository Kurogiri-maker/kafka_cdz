package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;

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
        String fileClassification = classifyFile(fileContent , getFilterParameters());

        Map<String,String> metadataStringMap = extractMetadataFromPdf(fileContent);

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
        String fileClassification = classifyFile(fileContent , getFilterParameters());

        Random random = new Random();

        Integer numero = random.nextInt(20);

        String collect = switch (fileClassification) {
            case "Tiers" ->
                    "{\"numero\":\""+numero+"\",\"nom\":\"test\",\"siren\":\"56789\",\"refMandat\":\"98765\"}";
            case "Dossier" ->
                    "{\"dossierDC\":\"facture\",\"numero\":\""+numero+"\",\"listSDC\":\"test\",\"n_DPS\":\"3\",\"montant_du_pres\":\"80\"}";
            //"{\"id\":\"%d\",\"dossier_DC\":\"%s\",\"numero\":\"%s\",\"listSDC\":\"%s\",\"n_DPS\":\"%s\",\"montant_du_pres\":\"%s\"}";

            case "Contrat" ->
                    "{\"numero\":\""+numero+"\",\"raisonSocial\":\"dUcyEW\",\"codeProduit\":\"lxbWJf\",\"produit\":\"SYRGmC\",\"phase\":\"WUKJHS\",\"montantPret\":\"WTwCQv\"}";
            default -> "unknown document type";
        };


        int randomInt = random.nextInt(4);
        for (int i = 1; i < randomInt+1; i++) {
            String randomString = RandomStringService.generateRandomString(7);
            collect= collect.substring(0,collect.length()-1) +",\"attribute "+i+"\":\""+randomString+"\"}" ;
        }
        return collect;

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

    private Map<String, String> extractMetadataFromPdf(byte[] fileBytes) throws IOException{
        PDDocument document = null;
        try {
            document = PDDocument.load(new ByteArrayInputStream(fileBytes));
            PDDocumentInformation metadata = document.getDocumentInformation();
            if (metadata != null) {
                Map<String,String> metadataStringMap = new LinkedHashMap<>();
                metadataStringMap.put("Title", metadata.getTitle());
                metadataStringMap.put("Author", metadata.getAuthor());
                metadataStringMap.put("Subject", metadata.getSubject());
                metadataStringMap.put("Keywords", metadata.getKeywords());
                metadataStringMap.put("Creator", metadata.getCreator());
                metadataStringMap.put("Producer", metadata.getProducer());
                // Get creation date
                Calendar creationDate = metadata.getCreationDate();
                Calendar modDate = metadata.getModificationDate();
                // Convert Calendar to Date
                Date creationDateAsDate = creationDate.getTime();
                Date modDateAsDate = modDate.getTime();
                // Format Date to String
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedCreationDate = dateFormat.format(creationDateAsDate);
                String formattedModDate = dateFormat.format(modDateAsDate);

                metadataStringMap.put("CreationDate", formattedCreationDate);
                metadataStringMap.put("ModDate", formattedModDate);

                return metadataStringMap;
            }else {
                log.info("Metadata not found");
            }


        } finally {
            if (document != null) {
                document.close();
            }
        }

        return null;
    }





}


// .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//.\bin\windows\kafka-server-start.bat .\config\server.properties