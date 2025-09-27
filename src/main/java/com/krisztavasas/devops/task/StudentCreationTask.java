package com.krisztavasas.devops.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krisztavasas.devops.entity.Student;
import com.krisztavasas.devops.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentCreationTask {

    private final StudentService studentService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 240000) // 4 minutes = 240,000 milliseconds
    public void createStudentsFromFile() {
        log.info("Running student creation task from students.json");

        try {
            ClassPathResource resource = new ClassPathResource("static/students.json");
            InputStream inputStream = resource.getInputStream();

            List<Student> students = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            for (Student student : students) {
                try {
                    if (!studentService.existsByEmail(student.getEmail())) {
                        studentService.addStudent(student);
                        log.debug("Created student: {}", student.getName());
                    } else {
                        log.debug("Student with email {} already exists, skipping", student.getEmail());
                    }
                } catch (Exception e) {
                    log.error("Error creating student: {}", student.getName(), e);
                }
            }

            log.info("Student creation task completed successfully");
        } catch (IOException e) {
            log.error("Error reading students.json file", e);
        } catch (Exception e) {
            log.error("Error during student creation task", e);
        }
    }
}