package com.instashortlist.backend.config;

import com.instashortlist.backend.scheduler.LinkedInJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail linkedInJobDetail() {
        return JobBuilder.newJob(LinkedInJob.class)
                .withIdentity("linkedInJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger linkedInTrigger(JobDetail linkedInJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(linkedInJobDetail)
                .withIdentity("linkedInTrigger")
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(60) // or change as needed
                        .repeatForever())
                .build();
    }
}
