package com.example.hogwarts.controller;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.service.StudentService;
import com.example.hogwarts.service.ThreadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final ThreadService threadService;

    public StudentController(StudentService studentService, ThreadService threadService) {
        this.studentService = studentService;
        this.threadService = threadService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.save(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student studentDetails) {

        return studentService.findById(id)
                .map(existingStudent -> {
                    existingStudent.setName(studentDetails.getName());
                    existingStudent.setAge(studentDetails.getAge());
                    existingStudent.setFaculty(studentDetails.getFaculty());
                    Student updated = studentService.save(existingStudent);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentService.findById(id).isPresent()) {
            studentService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/name/{name}")
    public List<Student> getStudentsByName(@PathVariable String name) {
        return studentService.findByName(name);
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeBetween(
            @RequestParam Integer min,
            @RequestParam Integer max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        return studentService.findById(id)
                .map(student -> ResponseEntity.ok(student.getFaculty()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public Long getCountOfStudents() {
        return studentService.countAllStudents();
    }

    @GetMapping("/last-5")
    public List<Student> getLast5Students() {
        return studentService.findLast5Students();
    }

    @GetMapping("/names-starts-with-a")
    public List<String> getStudentNamesStartsWithA() {
        return studentService.findAll()
                .stream()
                .filter(student -> student.getName().toUpperCase().startsWith("A"))
                .map(student -> student.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/average-age")
    public Double getAverageStudentAge() {
        return studentService.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @GetMapping("/students/print-parallel")
    public String printStudentsParallel() {
        List<Student> students = studentService.findAll();

        for (int i = 0; i < Math.min(2, students.size()); i++) {
            System.out.println("Main thread: " + students.get(i).getName());
        }

        if (students.size() > 3) {
            new Thread(() -> {
                for (int i = 2; i < Math.min(4, students.size()); i++) {
                    System.out.println("Thread 1: " + students.get(i).getName());
                }
            }).start();
        }

        if (students.size() > 5) {
            new Thread(() -> {
                for (int i = 4; i < Math.min(6, students.size()); i++) {
                    System.out.println("Thread 2: " + students.get(i).getName());
                }
            }).start();
        }

        return "Students printed ";
    }

    @GetMapping("/students/print-synchronized")
    public String printStudentsSynchronized() {
        List<Student> students = studentService.findAll();
        threadService.printStudentsSynchronized(students);
        return "Students printed ";
    }
}