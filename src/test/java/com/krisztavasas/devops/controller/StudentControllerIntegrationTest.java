package com.krisztavasas.devops.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisztavasas.devops.entity.Student;
import com.krisztavasas.devops.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllStudents_ShouldReturnStudentsList() throws Exception {
        UUID studentId = UUID.randomUUID();
        Student student1 = new Student(studentId, "John Doe", "john@example.com", "Devops", true);
        Student student2 = new Student(UUID.randomUUID(), "Jane Smith", "jane@example.com", "Computer Science", false);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        // Test GET /api/students
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john@example.com")))
                .andExpect(jsonPath("$[0].course", is("Devops")))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")))
                .andExpect(jsonPath("$[1].active", is(false)));
    }

    @Test
    void addStudent_ShouldCreateStudent() throws Exception {
        Student inputStudent = new Student(null, "John Doe", "john@example.com", "Devops", true);
        Student savedStudent = new Student(UUID.randomUUID(), "John Doe", "john@example.com", "Devops", true);

        when(studentService.addStudent(any(Student.class))).thenReturn(savedStudent);

        String studentJson = objectMapper.writeValueAsString(inputStudent);

        // Test POST /api/students
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.course", is("Devops")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deleteStudent_ShouldDeleteSuccessfully() throws Exception {
        UUID studentId = UUID.randomUUID();

        // Test DELETE /api/students/{id}
        mockMvc.perform(delete("/api/students/" + studentId))
                .andExpect(status().isNoContent());
    }
}