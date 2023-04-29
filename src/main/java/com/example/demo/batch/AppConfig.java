package com.example.demo.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AppConfig  {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Step myStep;
    @Autowired
    private Job myJob;





    @Scheduled(cron = "0 0/1 * 1/1 *  *")
    public void run() throws Exception {
        System.out.println("Job Started at :" + new Date());
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);
        System.out.println("Job Status :" +jobExecution.getStatus());
    }
}
