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
class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();

        testFaculty = new Faculty("Gryffindor", "Red and Gold");
        testFaculty = facultyRepository.save(testFaculty);
    }

    @Test
    void testCreateFaculty() {
        Faculty newFaculty = new Faculty("Slytherin", "Green and Silver");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "/faculty", newFaculty, Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Slytherin", response.getBody().getName());
        assertEquals("Green and Silver", response.getBody().getColor());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testGetAllFaculties() {
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "/faculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Gryffindor", response.getBody().get(0).getName());
    }

    @Test
    void testGetFacultyById() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/" + testFaculty.getId(), Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gryffindor", response.getBody().getName());
    }

    @Test
    void testGetFacultyByIdNotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/999", Faculty.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateFaculty() {
        testFaculty.setName("Gryffindor Updated");
        testFaculty.setColor("Scarlet and Gold");

        restTemplate.put("/faculty/" + testFaculty.getId(), testFaculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/" + testFaculty.getId(), Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Gryffindor Updated", response.getBody().getName());
        assertEquals("Scarlet and Gold", response.getBody().getColor());
    }

    @Test
    void testDeleteFaculty() {
        restTemplate.delete("/faculty/" + testFaculty.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty/" + testFaculty.getId(), Faculty.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSearchFacultyByName() {
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "/faculty/search?query=Gryffindor",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchFacultyByColor() {
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                "/faculty/search?query=Red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetFacultyStudents() {
        Student student1 = new Student("Harry Potter", 11);
        student1.setFaculty(testFaculty);
        studentRepository.save(student1);

        Student student2 = new Student("Hermione Granger", 11);
        student2.setFaculty(testFaculty);
        studentRepository.save(student2);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "/faculty/" + testFaculty.getId() + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}