package com.krisztavasas.devops.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class ErrorSimulationTask {

    private final Random random = new Random();

    @Scheduled(fixedRate = 180000) // 3 minutes = 180,000 milliseconds
    public void simulateError() {
        if (random.nextInt(100) < 10) { // 10% chance
            log.error("Scheduled task failed due to simulated error.");
            throw new RuntimeException("Simulated error for testing purposes");
        }

        log.debug("Error simulation task completed successfully (no error triggered)");
    }
}