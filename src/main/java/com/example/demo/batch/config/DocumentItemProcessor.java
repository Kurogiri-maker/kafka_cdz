package com.example.demo.batch.config;


import com.example.demo.batch.model.AdditionalAttribute;
import com.example.demo.batch.model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
@StepScope
public class DocumentItemProcessor implements ItemProcessor<Document,Map<Document , Map<String,String>>> {

    @Value("#{jobParameters['attribute']}")
    private String attribute;
    ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public Map<Document , Map<String,String>>  process(@NonNull Document document) throws Exception {
        System.out.println("////////////////////////////// Processing step ////////////////////////////////////////");
        AdditionalAttribute object = objectMapper.readValue(attribute, AdditionalAttribute.class);

        Random random = new Random();
        Boolean b = random.nextDouble()<0.75;
        Map<Document , Map<String,String>> documentProcessed = new HashMap<>();
        Map<String,String> attributeValueExtraced = new HashMap<>();

        if(b.equals(true)){
            String val = extractAttributeFromDocument(document , object.getAttribute());
            System.out.println("DocumentId : " +document.getId() + " Attribute :" + val );
            attributeValueExtraced.put(object.getAttribute(),val);
            documentProcessed.put(document,attributeValueExtraced);
        }

        return documentProcessed;
    }


    public String extractAttributeFromDocument(Document d , String attribute){
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = Math.min(random.nextInt(5), 5)+5;
        for(int i=0;i<length;i++){
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
