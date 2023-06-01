package com.example.demo.batch;


import com.example.demo.RandomStringService;
import com.example.demo.model.AdditionalAttribute;
import com.example.demo.model.Document;
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
            String val = RandomStringService.generateRandomString(7);
            System.out.println("DocumentId : " +document.getId() + " Attribute :" + val );
            attributeValueExtraced.put(object.getAttribute(),val);
            documentProcessed.put(document,attributeValueExtraced);
        }

        return documentProcessed;
    }

}
