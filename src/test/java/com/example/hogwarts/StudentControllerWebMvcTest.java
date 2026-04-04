package com.example.hogwarts;

import com.example.hogwarts.controller.StudentController;
import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
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

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student testStudent;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty("Gryffindor", "Red and Gold");
        testFaculty.setId(1L);

        testStudent = new Student("Harry Potter", 11);
        testStudent.setId(1L);
        testStudent.setFaculty(testFaculty);
    }

    @Test
    void testCreateStudent() throws Exception {
        when(studentService.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(11));

        verify(studentService, times(1)).save(any(Student.class));
    }

    @Test
    void testGetAllStudents() throws Exception {
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.findAll()).thenReturn(students);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));

        verify(studentService, times(1)).findAll();
    }

    @Test
    void testGetStudentById() throws Exception {
        when(studentService.findById(1L)).thenReturn(Optional.of(testStudent));

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"));

        verify(studentService, times(1)).findById(1L);
    }

    @Test
    void testGetStudentByIdNotFound() throws Exception {
        when(studentService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/999"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).findById(999L);
    }

    @Test
    void testUpdateStudent() throws Exception {
        testStudent.setName("Harry James Potter");
        when(studentService.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentService.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry James Potter"));

        verify(studentService, times(1)).findById(1L);
        verify(studentService, times(1)).save(any(Student.class));
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(studentService.findById(1L)).thenReturn(Optional.of(testStudent));
        doNothing().when(studentService).deleteById(1L);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).findById(1L);
        verify(studentService, times(1)).deleteById(1L);
    }

    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.findByAgeBetween(10, 15)).thenReturn(students);

        mockMvc.perform(get("/student/age-between")
                        .param("min", "10")
                        .param("max", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));

        verify(studentService, times(1)).findByAgeBetween(10, 15);
    }

    @Test
    void testGetStudentFaculty() throws Exception {
        when(studentService.findById(1L)).thenReturn(Optional.of(testStudent));

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red and Gold"));

        verify(studentService, times(1)).findById(1L);
    }
}