package com.example.demo.batch.config;

import com.example.demo.batch.model.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    @StepScope
    private DocumentItemProcessor processor;

    @Autowired
    @JobScope
    private DocumentItemReader reader;

    @Autowired
    @JobScope
    private DocumentItemWriter writer;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.afterPropertiesSet();
        return (JobRepository) factory.getObject();
    }


    @Bean
    public Step myStep(JobRepository jobRepository) throws Exception {
        return new StepBuilder("myStep")
                .<Document, Map<Document , Map<String,String>>>chunk(1)
                .reader(this.reader)
                .processor(this.processor)
                .writer(this.writer)
                .repository(jobRepository())
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job myJob() throws Exception {
        return new JobBuilder("myJob")
                .incrementer(new RunIdIncrementer())
                .start(myStep(jobRepository()))
                .repository(jobRepository())
                .listener(this.reader)
                .listener(this.writer)
                .build();

    }


}
