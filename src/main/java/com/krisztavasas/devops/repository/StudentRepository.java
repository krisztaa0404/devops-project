package com.krisztavasas.devops.repository;

import com.krisztavasas.devops.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Modifying
    @Query("DELETE FROM Student s WHERE s.active = false")
    int deleteInactiveStudents();
}