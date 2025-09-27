package com.krisztavasas.devops.task;

import com.krisztavasas.devops.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseCleanupTask {

    private final StudentService studentService;

    @Scheduled(fixedRate = 120000) // 2 minutes = 120,000 milliseconds
    public void cleanupInactiveStudents() {
        log.info("Running database cleanup task for inactive students");

        try {
            int deletedCount = studentService.deleteInactiveStudents();
            log.debug("Database cleanup completed - {} inactive students found and removed", deletedCount);
        } catch (Exception e) {
            log.error("Error during database cleanup task", e);
        }
    }
}