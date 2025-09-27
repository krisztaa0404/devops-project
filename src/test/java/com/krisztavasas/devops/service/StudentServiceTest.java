package com.krisztavasas.devops.service;

import com.krisztavasas.devops.entity.Student;
import com.krisztavasas.devops.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testStudent = new Student(testId, "John Doe", "john@example.com", "Devops", true);
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        List<Student> expectedStudents = Arrays.asList(testStudent, new Student());
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        List<Student> result = studentService.getAllStudents();

        assertEquals(expectedStudents, result);
        verify(studentRepository).findAll();
    }

    @Test
    void getAllStudents_ShouldThrowException_WhenRepositoryFails() {
        when(studentRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.getAllStudents());

        assertEquals("Failed to retrieve students", exception.getMessage());
        verify(studentRepository).findAll();
    }

    @Test
    void addStudent_ShouldSaveAndReturnStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student result = studentService.addStudent(testStudent);

        assertEquals(testStudent, result);
        verify(studentRepository).save(testStudent);
    }

    @Test
    void addStudent_ShouldThrowException_WhenEmailAlreadyExists() {
        when(studentRepository.save(any(Student.class))).thenThrow(new DataIntegrityViolationException("Duplicate email"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.addStudent(testStudent));

        assertEquals("A student with this email already exists", exception.getMessage());
        verify(studentRepository).save(testStudent);
    }

    @Test
    void addStudent_ShouldThrowException_WhenUnexpectedErrorOccurs() {
        when(studentRepository.save(any(Student.class))).thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.addStudent(testStudent));

        assertEquals("Failed to add student", exception.getMessage());
        verify(studentRepository).save(testStudent);
    }

    @Test
    void updateStudent_ShouldUpdateAndReturnStudent() {
        Student updatedStudent = new Student(null, "Jane Doe", "jane@example.com", "Mathematics", false);
        when(studentRepository.findById(testId)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student result = studentService.updateStudent(testId, updatedStudent);

        assertEquals(testStudent, result);
        verify(studentRepository).findById(testId);
        verify(studentRepository).save(testStudent);
        assertEquals("Jane Doe", testStudent.getName());
        assertEquals("jane@example.com", testStudent.getEmail());
        assertEquals("Mathematics", testStudent.getCourse());
        assertEquals(false, testStudent.getActive());
    }

    @Test
    void updateStudent_ShouldThrowException_WhenStudentNotFound() {
        Student updatedStudent = new Student();
        when(studentRepository.findById(testId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.updateStudent(testId, updatedStudent));

        assertEquals("Failed to update student", exception.getMessage());
        verify(studentRepository).findById(testId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateStudent_ShouldThrowException_WhenEmailAlreadyExists() {
        Student updatedStudent = new Student(null, "Jane Doe", null, null, null);
        when(studentRepository.findById(testId)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenThrow(new DataIntegrityViolationException("Duplicate email"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.updateStudent(testId, updatedStudent));

        assertEquals("Email already exists for another student", exception.getMessage());
        verify(studentRepository).findById(testId);
        verify(studentRepository).save(testStudent);
    }

    @Test
    void updateStudent_ShouldOnlyUpdateNonNullFields() {
        Student partialUpdate = new Student(null, "Jane Doe", null, null, false);
        when(studentRepository.findById(testId)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        studentService.updateStudent(testId, partialUpdate);

        assertEquals("Jane Doe", testStudent.getName());
        assertEquals("john@example.com", testStudent.getEmail()); // unchanged
        assertEquals("Devops", testStudent.getCourse()); // unchanged
        assertEquals(false, testStudent.getActive());
    }

    @Test
    void deleteStudent_ShouldDeleteStudent_WhenStudentExists() {
        when(studentRepository.existsById(testId)).thenReturn(true);

        assertDoesNotThrow(() -> studentService.deleteStudent(testId));

        verify(studentRepository).existsById(testId);
        verify(studentRepository).deleteById(testId);
    }

    @Test
    void deleteStudent_ShouldThrowException_WhenStudentNotFound() {
        when(studentRepository.existsById(testId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.deleteStudent(testId));

        assertEquals("Failed to delete student", exception.getMessage());
        verify(studentRepository).existsById(testId);
        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void deleteStudent_ShouldThrowException_WhenDeletionFails() {
        when(studentRepository.existsById(testId)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(studentRepository).deleteById(testId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.deleteStudent(testId));

        assertEquals("Failed to delete student", exception.getMessage());
        verify(studentRepository).existsById(testId);
        verify(studentRepository).deleteById(testId);
    }

    @Test
    void deleteInactiveStudents_ShouldReturnDeletedCount() {
        int expectedDeletedCount = 5;
        when(studentRepository.deleteInactiveStudents()).thenReturn(expectedDeletedCount);

        int result = studentService.deleteInactiveStudents();

        assertEquals(expectedDeletedCount, result);
        verify(studentRepository).deleteInactiveStudents();
    }

    @Test
    void deleteInactiveStudents_ShouldThrowException_WhenDeletionFails() {
        when(studentRepository.deleteInactiveStudents()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.deleteInactiveStudents());

        assertEquals("Failed to delete inactive students", exception.getMessage());
        verify(studentRepository).deleteInactiveStudents();
    }
}