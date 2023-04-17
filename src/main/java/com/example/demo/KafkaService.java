package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
public class KafkaService {

    public static String extractTextFromPdf(byte[] fileBytes) throws IOException {
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
    public static String classifyFile(byte[] fileContent, List<FilterParameter> filterParameters) throws IOException {

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

    public static List<FilterParameter> getFilterParameters() {
        List<FilterParameter> filterParameters = new ArrayList<>();
        filterParameters.add(new FilterParameter("siren", "Tiers"));
        filterParameters.add(new FilterParameter("ListSDC", "Dossier"));
        return filterParameters;
    }

    public static Map<String, String> extractMetadataFromPdf(byte[] fileBytes) throws IOException{
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
