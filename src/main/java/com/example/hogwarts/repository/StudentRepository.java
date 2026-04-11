package com.example.hogwarts.repository;

import com.example.hogwarts.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByName(String name);
    List<Student> findByFacultyId(Long facultyId);
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query("SELECT COUNT(s) FROM Student s")
    Long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageStudentAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    List<Student> findLast5Students();
}