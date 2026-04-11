package com.example.hogwarts;

import com.example.hogwarts.controller.FacultyController;
import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.service.FacultyService;
import com.example.hogwarts.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private StudentService studentService;

    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty("Gryffindor", "Red and Gold");
        testFaculty.setId(1L);
    }

    @Test
    void testCreateFaculty() throws Exception {
        when(facultyService.save(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red and Gold"));

        verify(facultyService, times(1)).save(any(Faculty.class));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(testFaculty);
        when(facultyService.findAll()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));

        verify(facultyService, times(1)).findAll();
    }

    @Test
    void testGetFacultyById() throws Exception {
        when(facultyService.findById(1L)).thenReturn(Optional.of(testFaculty));

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"));

        verify(facultyService, times(1)).findById(1L);
    }

    @Test
    void testGetFacultyByIdNotFound() throws Exception {
        when(facultyService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/faculty/999"))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).findById(999L);
    }

    @Test
    void testUpdateFaculty() throws Exception {
        testFaculty.setName("Gryffindor Updated");
        testFaculty.setColor("Scarlet and Gold");
        when(facultyService.findById(1L)).thenReturn(Optional.of(testFaculty));
        when(facultyService.save(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor Updated"))
                .andExpect(jsonPath("$.color").value("Scarlet and Gold"));

        verify(facultyService, times(1)).findById(1L);
        verify(facultyService, times(1)).save(any(Faculty.class));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        when(facultyService.findById(1L)).thenReturn(Optional.of(testFaculty));
        doNothing().when(facultyService).deleteById(1L);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).findById(1L);
        verify(facultyService, times(1)).deleteById(1L);
    }

    @Test
    void testSearchFaculty() throws Exception {
        List<Faculty> faculties = Arrays.asList(testFaculty);
        when(facultyService.searchByNameOrColor("Gryffindor")).thenReturn(faculties);

        mockMvc.perform(get("/faculty/search")
                        .param("query", "Gryffindor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));

        verify(facultyService, times(1)).searchByNameOrColor("Gryffindor");
    }

    @Test
    void testGetFacultyStudents() throws Exception {
        Student student1 = new Student("Harry Potter", 11);
        student1.setId(1L);
        List<Student> students = Arrays.asList(student1);
        when(studentService.findByFacultyId(1L)).thenReturn(students);

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));

        verify(studentService, times(1)).findByFacultyId(1L);
    }
}