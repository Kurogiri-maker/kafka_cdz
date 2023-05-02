package com.example.demo.batch.config;

import com.example.demo.TopicProducer;
import com.example.demo.batch.model.Document;
import com.example.demo.batch.repository.DocumentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@JobScope
public class DocumentItemWriter implements ItemWriter<Map<Document , Map<String,String>>> , JobExecutionListener {

    private final DocumentRepository repository;

    private List<Map<Document,Map<String,String>>> documentsProcessed = new ArrayList<>();

    @Autowired
    private TopicProducer topicProducer;




    @Override
    public void write(List<? extends Map<Document , Map<String,String>>> docs) throws Exception {
        System.out.println("////////////////////////////// Writing step ////////////////////////////////////////");

        docs.forEach(document -> {

                documentsProcessed.add(document);
                System.out.println("Document : "+ document + " is processed");

        });
        System.out.println(documentsProcessed.size());

    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        var ref = new Object() {
            public void setCompteur(int compteur) {
                this.compteur = compteur;
            }

            public int getCompteur() {
                return compteur;
            }

            private int compteur = 0;
        };

        documentsProcessed.forEach(map -> {
            map.forEach((document, value) -> {
                if(value.values().iterator().hasNext()){
                    ref.setCompteur(ref.getCompteur() + 1);
                }
            });
        } );

        if(documentsProcessed.size() == 0){
            System.out.println("This is attribute is not valid \n" + "The percentage of documents who has a value for this attribute is 0.0%");
        }else{
            float percentage = (float) ref.getCompteur()/documentsProcessed.size();
            if( percentage >= 0.8 ){
                System.out.println("This is attribute is valid \n" + "The percentage of documents who has a value for this attribute is " + (percentage * 100 )+"%");
                documentsProcessed.forEach(doc -> {
                    try {
                        topicProducer.sendDocument(doc);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }else {
                System.out.println("This is attribute is not valid \n" + "The percentage of documents who has a value for this attribute is " + (percentage * 100 )+"%");
            }

        }


    }
}
