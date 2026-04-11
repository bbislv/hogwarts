package com.example.hogwarts.service;

import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) {
        logger.info("Was invoked method for create/update student");
        logger.debug("Saving student with name: {}", student.getName());
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        logger.info("Was invoked method for find all students");
        logger.debug("Finding all students");
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        logger.info("Was invoked method for find student by id: {}", id);
        logger.debug("Searching student with id: {}", id);
        return studentRepository.findById(id);
    }

    public void deleteById(Long id) {
        logger.info("Was invoked method for delete student by id: {}", id);
        if (studentRepository.existsById(id)) {
            logger.debug("Deleting student with id: {}", id);
            studentRepository.deleteById(id);
        } else {
            logger.warn("Student with id {} not found for deletion", id);
        }
    }

    public List<Student> findByName(String name) {
        logger.info("Was invoked method for find students by name: {}", name);
        logger.debug("Searching students with name: {}", name);
        return studentRepository.findByName(name);
    }

    public List<Student> findByAgeBetween(Integer minAge, Integer maxAge) {
        logger.info("Was invoked method for find students by age between {} and {}", minAge, maxAge);
        logger.debug("Searching students with age from {} to {}", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> findByFacultyId(Long facultyId) {
        logger.info("Was invoked method for find students by faculty id: {}", facultyId);
        logger.debug("Searching students with faculty id: {}", facultyId);
        return studentRepository.findByFacultyId(facultyId);
    }

    public Long countAllStudents() {
        logger.info("Was invoked method for count all students");
        return studentRepository.countAllStudents();
    }

    public Double getAverageStudentAge() {
        logger.info("Was invoked method for calculate average student age");
        return studentRepository.getAverageStudentAge();
    }

    public List<Student> findLast5Students() {
        logger.info("Was invoked method for find last 5 students");
        return studentRepository.findLast5Students();
    }
}