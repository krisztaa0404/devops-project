package com.krisztavasas.devops.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisztavasas.devops.entity.Student;
import com.krisztavasas.devops.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentCreationTaskTest {

    @Mock
    private StudentService studentService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StudentCreationTask studentCreationTask;

    private Student testStudent1;
    private Student testStudent2;
    private List<Student> studentList;

    @BeforeEach
    void setUp() {
        testStudent1 = new Student(null, "John Smith", "john.smith@example.com", "Computer Science", false);
        testStudent2 = new Student(null, "Emily Johnson", "emily.johnson@example.com", "Mathematics", false);
        studentList = Arrays.asList(testStudent1, testStudent2);
    }

    @Test
    void createStudentsFromFile_ShouldCreateNewStudents_WhenStudentsDoNotExist() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenReturn(studentList);
        when(studentService.existsByEmail("john.smith@example.com")).thenReturn(false);
        when(studentService.existsByEmail("emily.johnson@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> studentCreationTask.createStudentsFromFile());

        verify(studentService).existsByEmail("john.smith@example.com");
        verify(studentService).existsByEmail("emily.johnson@example.com");
        verify(studentService).addStudent(testStudent1);
        verify(studentService).addStudent(testStudent2);
    }

    @Test
    void createStudentsFromFile_ShouldSkipExistingStudents_WhenStudentsAlreadyExist() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenReturn(studentList);
        when(studentService.existsByEmail("john.smith@example.com")).thenReturn(true);
        when(studentService.existsByEmail("emily.johnson@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> studentCreationTask.createStudentsFromFile());

        verify(studentService).existsByEmail("john.smith@example.com");
        verify(studentService).existsByEmail("emily.johnson@example.com");
        verify(studentService, never()).addStudent(testStudent1);
        verify(studentService).addStudent(testStudent2);
    }

    @Test
    void createStudentsFromFile_ShouldHandleJsonParsingException_WhenInvalidJson() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenThrow(new IOException("Invalid JSON"));

        assertDoesNotThrow(() -> studentCreationTask.createStudentsFromFile());

        verify(studentService, never()).existsByEmail(anyString());
        verify(studentService, never()).addStudent(any(Student.class));
    }

    @Test
    void createStudentsFromFile_ShouldContinueProcessing_WhenOneStudentFails() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenReturn(studentList);
        when(studentService.existsByEmail("john.smith@example.com")).thenReturn(false);
        when(studentService.existsByEmail("emily.johnson@example.com")).thenReturn(false);
        when(studentService.addStudent(testStudent1)).thenThrow(new RuntimeException("Database error"));

        assertDoesNotThrow(() -> studentCreationTask.createStudentsFromFile());

        verify(studentService).existsByEmail("john.smith@example.com");
        verify(studentService).existsByEmail("emily.johnson@example.com");
        verify(studentService).addStudent(testStudent1);
        verify(studentService).addStudent(testStudent2);
    }

    @Test
    void createStudentsFromFile_ShouldHandleServiceException_WhenExistsByEmailFails() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenReturn(Collections.singletonList(testStudent1));
        when(studentService.existsByEmail("john.smith@example.com")).thenThrow(new RuntimeException("Service error"));

        assertDoesNotThrow(() -> studentCreationTask.createStudentsFromFile());

        verify(studentService).existsByEmail("john.smith@example.com");
        verify(studentService, never()).addStudent(any(Student.class));
    }
}