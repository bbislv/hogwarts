package com.example.hogwarts.controller;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.service.FacultyService;
import com.example.hogwarts.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;
    private final StudentService studentService;

    public FacultyController(FacultyService facultyService, StudentService studentService) {
        this.facultyService = facultyService;
        this.studentService = studentService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.save(faculty);
    }

    @GetMapping
    public List<Faculty> getAllFaculties() {
        return facultyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        return facultyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable Long id,
            @RequestBody Faculty facultyDetails) {

        return facultyService.findById(id)
                .map(existingFaculty -> {
                    existingFaculty.setName(facultyDetails.getName());
                    existingFaculty.setColor(facultyDetails.getColor());
                    Faculty updated = facultyService.save(existingFaculty);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        if (facultyService.findById(id).isPresent()) {
            facultyService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/name/{name}")
    public Faculty getFacultyByName(@PathVariable String name) {
        return facultyService.findByName(name);
    }

    @GetMapping("/search")
    public List<Faculty> searchFaculty(@RequestParam String query) {
        return facultyService.searchByNameOrColor(query);
    }

    @GetMapping("/{id}/students")
    public List<Student> getFacultyStudents(@PathVariable Long id) {
        return studentService.findByFacultyId(id);
    }

    @GetMapping("/longest-name")
    public String getLongestFacultyName() {
        return facultyService.findAll()
                .stream()
                .max(Comparator.comparingInt(faculty -> faculty.getName().length()))
                .map(Faculty::getName)
                .orElse("");
    }
}