package com.krisztavasas.devops.service;

import com.krisztavasas.devops.entity.Student;
import com.krisztavasas.devops.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        try {
            log.info("Retrieving all students");
            List<Student> students = studentRepository.findAll();
            log.info("Found {} students", students.size());
            return students;
        } catch (Exception e) {
            log.error("Error retrieving all students", e);
            throw new RuntimeException("Failed to retrieve students", e);
        }
    }

    @Transactional
    public Student addStudent(Student student) {
        try {
            log.info("Adding new student with email: {}", student.getEmail());
            Student savedStudent = studentRepository.save(student);
            log.info("Successfully added student with ID: {} and email: {}", savedStudent.getId(), savedStudent.getEmail());
            return savedStudent;
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when adding student with email: {}", student.getEmail(), e);
            throw new RuntimeException("A student with this email already exists", e);
        } catch (Exception e) {
            log.error("Unexpected error when adding student with email: {}", student.getEmail(), e);
            throw new RuntimeException("Failed to add student", e);
        }
    }

    @Transactional
    public Student updateStudent(UUID id, Student updatedStudent) {
        try {
            log.info("Updating student with ID: {}", id);

            Student existingStudent = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

            if (updatedStudent.getName() != null) {
                existingStudent.setName(updatedStudent.getName());
            }

            if (updatedStudent.getEmail() != null) {
                existingStudent.setEmail(updatedStudent.getEmail());
            }

            if (updatedStudent.getCourse() != null) {
                existingStudent.setCourse(updatedStudent.getCourse());
            }

            if (updatedStudent.getActive() != null) {
                existingStudent.setActive(updatedStudent.getActive());
            }

            Student savedStudent = studentRepository.save(existingStudent);
            log.info("Successfully updated student with ID: {}", savedStudent.getId());
            return savedStudent;
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when updating student with ID: {}", id, e);
            throw new RuntimeException("Email already exists for another student", e);
        } catch (Exception e) {
            log.error("Unexpected error when updating student with ID: {}", id, e);
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Transactional
    public void deleteStudent(UUID id) {
        try {
            log.info("Deleting student with ID: {}", id);

            if (!studentRepository.existsById(id)) {
                throw new RuntimeException("Student not found with ID: " + id);
            }

            studentRepository.deleteById(id);
            log.info("Successfully deleted student with ID: {}", id);
        } catch (Exception e) {
            log.error("Error when deleting student with ID: {}", id, e);
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    public boolean existsByEmail(String email) {
        try {
            log.debug("Checking if student exists with email: {}", email);
            return studentRepository.existsByEmail(email);
        } catch (Exception e) {
            log.error("Error checking if student exists with email: {}", email, e);
            return false;
        }
    }

    @Transactional
    public int deleteInactiveStudents() {
        try {
            log.info("Deleting all inactive students");

            int deletedCount = studentRepository.deleteInactiveStudents();
            log.info("Successfully deleted {} inactive students", deletedCount);
            return deletedCount;
        } catch (Exception e) {
            log.error("Error when deleting inactive students", e);
            throw new RuntimeException("Failed to delete inactive students", e);
        }
    }
}