package com.krisztavasas.devops.controller;

import com.krisztavasas.devops.entity.Student;
import com.krisztavasas.devops.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student testStudent;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testStudent = new Student(testId, "John Doe", "john@example.com", "Devops", true);
    }

    @Test
    void getAllStudents_ShouldReturnOkWithStudents() {
        List<Student> expectedStudents = Arrays.asList(testStudent, new Student());
        when(studentService.getAllStudents()).thenReturn(expectedStudents);

        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudents, response.getBody());
        verify(studentService).getAllStudents();
    }

    @Test
    void getAllStudents_ShouldReturnInternalServerError_WhenServiceFails() {
        when(studentService.getAllStudents()).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).getAllStudents();
    }

    @Test
    void addStudent_ShouldReturnCreatedWithStudent() {
        when(studentService.addStudent(any(Student.class))).thenReturn(testStudent);

        ResponseEntity<Student> response = studentController.addStudent(testStudent);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testStudent, response.getBody());
        verify(studentService).addStudent(testStudent);
    }

    @Test
    void addStudent_ShouldReturnBadRequest_WhenIllegalArgumentException() {
        when(studentService.addStudent(any(Student.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<Student> response = studentController.addStudent(testStudent);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).addStudent(testStudent);
    }

    @Test
    void addStudent_ShouldReturnConflict_WhenRuntimeException() {
        when(studentService.addStudent(any(Student.class))).thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<Student> response = studentController.addStudent(testStudent);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).addStudent(testStudent);
    }

    @Test
    void addStudent_ShouldReturnConflict_WhenNonIllegalArgumentRuntimeException() {
        when(studentService.addStudent(any(Student.class))).thenThrow(new NullPointerException("Unexpected error"));

        ResponseEntity<Student> response = studentController.addStudent(testStudent);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).addStudent(testStudent);
    }

    @Test
    void deleteStudent_ShouldReturnNoContent_WhenDeletionSuccessful() {
        doNothing().when(studentService).deleteStudent(testId);

        ResponseEntity<Void> response = studentController.deleteStudent(testId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).deleteStudent(testId);
    }

    @Test
    void deleteStudent_ShouldReturnNotFound_WhenRuntimeException() {
        doThrow(new RuntimeException("Student not found")).when(studentService).deleteStudent(testId);

        ResponseEntity<Void> response = studentController.deleteStudent(testId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).deleteStudent(testId);
    }

    @Test
    void deleteStudent_ShouldReturnNotFound_WhenNonRuntimeException() {
        doThrow(new NullPointerException("Unexpected error")).when(studentService).deleteStudent(testId);

        ResponseEntity<Void> response = studentController.deleteStudent(testId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(studentService).deleteStudent(testId);
    }
}