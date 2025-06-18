package com.instashortlist.backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DummyScheduler {

    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void fetchData() {
        System.out.println("Scheduled job executed at: " + java.time.LocalDateTime.now());
        // TODO: Fetch LinkedIn data when ready
    }
}
