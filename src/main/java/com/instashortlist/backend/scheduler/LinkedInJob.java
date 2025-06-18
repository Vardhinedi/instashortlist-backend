package com.instashortlist.backend.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class LinkedInJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("🔁 Fetching LinkedIn jobs... " + java.time.LocalDateTime.now());
        // TODO: Implement actual job fetching logic later
    }
}
