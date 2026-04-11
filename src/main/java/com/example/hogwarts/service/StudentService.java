package com.example.hogwarts.service;

import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    public List<Student> findByAgeBetween(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> findByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }

    public Long countAllStudents() {
        return studentRepository.countAllStudents();
    }

    public Double getAverageStudentAge() {
        return studentRepository.getAverageStudentAge();
    }

    public List<Student> findLast5Students() {
        return studentRepository.findLast5Students();
    }
}