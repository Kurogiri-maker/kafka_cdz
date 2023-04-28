package com.example.demo.batch;

import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DocumentItemReader implements ItemReader<Document> {

    private final FlatFileItemReader<Document> reader;

    public DocumentItemReader() {
        reader = new FlatFileItemReaderBuilder<Document>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("numero", "filePath")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Document>() {{
                    setTargetType(Document.class);
                }})
                .build();
    }

    @Override
    public Document read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException{
        reader.open(new ExecutionContext());
        return reader.read();
    }
}
