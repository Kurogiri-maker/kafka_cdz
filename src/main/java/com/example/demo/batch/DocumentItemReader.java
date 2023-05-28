package com.example.demo.batch;


import com.example.demo.model.AdditionalAttribute;
import com.example.demo.model.Document;
import com.example.demo.repository.DocumentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@JobScope
public class DocumentItemReader implements ItemReader<Document> , JobExecutionListener {


    @Value("#{jobParameters['attribute']}")
    private String attribute;

    private   IteratorItemReader<?> reader;

    @Autowired
    private DocumentRepository docsRepository;

    List<Document> documents = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();




    @Override
    public void beforeJob(JobExecution jobExecution) {
        try {
            AdditionalAttribute object = objectMapper.readValue(attribute, AdditionalAttribute.class);
            documents = this.docsRepository.findAllByType(object.getType());
            reader = new IteratorItemReader<>(documents);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }

    @Override
    public Document read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException{
        System.out.println("////////////////////////////// Reading step ////////////////////////////////////////");
        return (Document) reader.read();
    }

}
