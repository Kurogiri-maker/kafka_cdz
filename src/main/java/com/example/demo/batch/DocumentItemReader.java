package com.example.demo.batch;

import org.springframework.batch.item.*;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentItemReader implements ItemReader<Document> {

    //private final FlatFileItemReader<Document> reader;

    private final IteratorItemReader<?> reader;

    @Autowired
    private final DocumentRepository repository;

    public DocumentItemReader(DocumentRepository  repository) {
        this.repository = repository;
       /* reader = new FlatFileItemReaderBuilder<Document>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("numero", "filePath")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Document>() {{
                    setTargetType(Document.class);
                }})
                .build();
        */
        List<Document> documents = this.repository.findAll();
        reader = new IteratorItemReader<>(documents);

    }

    @Override
    public Document read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException{
        /*
        reader.open(new ExecutionContext());
        return reader.read();
         */

        return (Document) reader.read();
    }
}
