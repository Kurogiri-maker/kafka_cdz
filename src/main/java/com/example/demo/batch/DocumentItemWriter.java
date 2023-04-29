package com.example.demo.batch;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class DocumentItemWriter implements ItemWriter<Document> {

    private final DocumentRepository repository;



    @Override
    public void write(List<? extends Document> docs) throws Exception {
        docs.forEach(document -> {
            System.out.println("Document : "+ document + " is processed");
        });
        repository.saveAll(docs);
    }
}
