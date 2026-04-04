package com.example.hogwarts;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Faculty testFaculty;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();

        testFaculty = new Faculty("Gryffindor", "Red and Gold");
        testFaculty = facultyRepository.save(testFaculty);

        testStudent = new Student("Harry Potter", 11);
        testStudent.setFaculty(testFaculty);
        testStudent = studentRepository.save(testStudent);
    }

    @Test
    void testCreateStudent() {
        Student newStudent = new Student("Hermione Granger", 11);
        newStudent.setFaculty(testFaculty);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student", newStudent, Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hermione Granger", response.getBody().getName());
        assertEquals(11, response.getBody().getAge());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testGetAllStudents() {
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Harry Potter", response.getBody().get(0).getName());
    }

    @Test
    void testGetStudentById() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/" + testStudent.getId(), Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Harry Potter", response.getBody().getName());
    }

    @Test
    void testGetStudentByIdNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/999", Student.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateStudent() {
        testStudent.setName("Harry James Potter");
        testStudent.setAge(12);

        restTemplate.put("/student/" + testStudent.getId(), testStudent);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/" + testStudent.getId(), Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Harry James Potter", response.getBody().getName());
        assertEquals(12, response.getBody().getAge());
    }

    @Test
    void testDeleteStudent() {
        restTemplate.delete("/student/" + testStudent.getId());

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "/student/" + testStudent.getId(), Student.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetStudentsByAgeBetween() {
        Student student2 = new Student("Ron Weasley", 11);
        student2.setFaculty(testFaculty);
        studentRepository.save(student2);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/student/age-between?min=10&max=11",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetStudentFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/student/" + testStudent.getId() + "/faculty", Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gryffindor", response.getBody().getName());
    }
}