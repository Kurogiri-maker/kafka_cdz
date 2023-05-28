package com.example.demo.config;

import com.example.demo.mapper.AdditionalAttributeMapper;
import com.example.demo.model.AdditionalAttribute;
import com.example.demo.repository.AdditionalAttributeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AppConfig  {

    @Bean
    public AdditionalAttributeMapper additionalAttributeMapper() {
        return Mappers.getMapper(AdditionalAttributeMapper.class);
    }

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Step myStep;
    @Autowired
    private Job myJob;

    @Autowired
    private AdditionalAttributeRepository repository;

    ObjectMapper mapper = new ObjectMapper();






    @Scheduled(cron = "0 0/1 * 1/1 *  *")
    public void run() throws Exception {
        List<AdditionalAttribute> attributes = repository.findAll();
        for (AdditionalAttribute attribute : attributes) {
            String object = mapper.writeValueAsString(attribute);
            System.out.println("Job Started at :" + new Date());
            System.out.println("Processing documents of type : " + attribute.getType());
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("attribute", object)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);
            System.out.println("Job Status :" + jobExecution.getStatus());
        }
    }
}
