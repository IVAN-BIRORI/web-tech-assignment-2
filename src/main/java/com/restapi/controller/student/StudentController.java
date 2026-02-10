package com.restapi.controller.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restapi.model.student.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> students = new ArrayList<>();
    private Long nextId = 6L;

    public StudentController() {
        students.add(new Student(1L, "John", "Doe", "john@example.com", "Computer Science", 3.8));
        students.add(new Student(2L, "Jane", "Smith", "jane@example.com", "Computer Science", 3.9));
        students.add(new Student(3L, "Bob", "Johnson", "bob@example.com", "Mathematics", 3.5));
        students.add(new Student(4L, "Alice", "Brown", "alice@example.com", "Physics", 3.6));
        students.add(new Student(5L, "Charlie", "Davis", "charlie@example.com", "Computer Science", 3.4));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        return students.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .map(student -> ResponseEntity.ok((Object) student))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student with ID " + studentId + " not found"));
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> results = students.stream()
                .filter(student -> student.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGpa(@RequestParam Double gpa) {
        List<Student> results = students.stream()
                .filter(student -> student.getGpa() >= gpa)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        student.setStudentId(nextId++);
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId, @RequestBody Student updatedStudent) {
        return students.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .map(student -> {
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setMajor(updatedStudent.getMajor());
                    student.setGpa(updatedStudent.getGpa());
                    return ResponseEntity.ok((Object) student);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student with ID " + studentId + " not found"));
    }
}
