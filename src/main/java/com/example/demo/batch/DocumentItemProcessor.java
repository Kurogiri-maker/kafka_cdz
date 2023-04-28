package com.example.demo.batch;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class DocumentItemProcessor implements ItemProcessor<Document,Document> {

    @Override
    public Document process(@NonNull Document document) throws Exception {
        String numero = document.getNumero();
        String path = document.getFilePath();

        Document d = new Document();
        d.setNumero(numero);
        d.setFilePath(path);
        System.out.println("Processor : " +d);

        // Pass document to ocr to extract data from it

        return d;
    }
}
