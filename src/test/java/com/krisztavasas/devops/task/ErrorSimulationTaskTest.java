package com.krisztavasas.devops.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ErrorSimulationTaskTest {

    private ErrorSimulationTask errorSimulationTask;

    @BeforeEach
    void setUp() {
        errorSimulationTask = new ErrorSimulationTask();
    }

    @Test
    void simulateError_ShouldSometimesThrowException() {
        int totalRuns = 1000;
        int exceptionCount = 0;

        for (int i = 0; i < totalRuns; i++) {
            try {
                errorSimulationTask.simulateError();
            } catch (RuntimeException e) {
                exceptionCount++;
                assertEquals("Simulated error for testing purposes", e.getMessage());
            }
        }

        assertTrue(exceptionCount >= 50, "Expected at least 50 exceptions out of 1000 runs, got: " + exceptionCount);
        assertTrue(exceptionCount <= 150, "Expected at most 150 exceptions out of 1000 runs, got: " + exceptionCount);
    }

    @Test
    void simulateError_ShouldThrowRuntimeExceptionWithCorrectMessage() {
        RuntimeException caughtException = null;
        int maxAttempts = 200;

        for (int i = 0; i < maxAttempts && caughtException == null; i++) {
            try {
                errorSimulationTask.simulateError();
            } catch (RuntimeException e) {
                caughtException = e;
            }
        }

        assertNotNull(caughtException, "Should have caught at least one exception in " + maxAttempts + " attempts");
        assertEquals("Simulated error for testing purposes", caughtException.getMessage());
    }

    @Test
    void simulateError_ShouldMostlyCompleteWithoutException() {
        int totalRuns = 100;
        int successCount = 0;

        for (int i = 0; i < totalRuns; i++) {
            try {
                errorSimulationTask.simulateError();
                successCount++;
            } catch (RuntimeException e) {
                // Expected occasionally
            }
        }

        assertTrue(successCount >= 70, "Expected at least 70 successful runs out of 100, got: " + successCount);
    }
}