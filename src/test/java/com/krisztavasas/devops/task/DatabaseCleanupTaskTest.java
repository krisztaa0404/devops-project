package com.krisztavasas.devops.task;

import com.krisztavasas.devops.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseCleanupTaskTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private DatabaseCleanupTask databaseCleanupTask;

    @Test
    void cleanupInactiveStudents_ShouldCallServiceMethod() {
        int expectedDeletedCount = 5;
        when(studentService.deleteInactiveStudents()).thenReturn(expectedDeletedCount);

        assertDoesNotThrow(() -> databaseCleanupTask.cleanupInactiveStudents());

        verify(studentService).deleteInactiveStudents();
    }

    @Test
    void cleanupInactiveStudents_ShouldHandleServiceException() {
        when(studentService.deleteInactiveStudents()).thenThrow(new RuntimeException("Database error"));

        assertDoesNotThrow(() -> databaseCleanupTask.cleanupInactiveStudents());

        verify(studentService).deleteInactiveStudents();
    }

    @Test
    void cleanupInactiveStudents_ShouldNotPropagateExceptions() {
        when(studentService.deleteInactiveStudents()).thenThrow(new RuntimeException("Service failure"));

        assertDoesNotThrow(() -> databaseCleanupTask.cleanupInactiveStudents());

        verify(studentService).deleteInactiveStudents();
    }
}