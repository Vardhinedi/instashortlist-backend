package com.instashortlist.backend.config;

import com.instashortlist.backend.jobs.ApplicationAutoProcessorJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail applicationJobDetail() {
        return JobBuilder.newJob(ApplicationAutoProcessorJob.class)
                .withIdentity("applicationAutoProcessorJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger applicationJobTrigger(JobDetail applicationJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(applicationJobDetail)
                .withIdentity("applicationAutoProcessorTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1) // ⏱ every 10 minutes
                        .repeatForever())
                .build();
    }
}
