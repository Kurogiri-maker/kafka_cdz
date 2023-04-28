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
        System.out.println("Writer : "+ docs);
        repository.saveAll(docs);
        System.out.println(repository.findAll());
    }
}
