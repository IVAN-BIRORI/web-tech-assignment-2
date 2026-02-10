package com.restapi.controller.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restapi.model.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 6L;

    public TaskController() {
        tasks.add(new Task(1L, "Complete Project", "Finish the Spring Boot project", false, "HIGH", "2026-02-15"));
        tasks.add(new Task(2L, "Review Code", "Review team's code changes", false, "MEDIUM", "2026-02-12"));
        tasks.add(new Task(3L, "Update Documentation", "Update API documentation", true, "LOW", "2026-02-10"));
        tasks.add(new Task(4L, "Fix Bugs", "Fix reported bugs in the application", false, "HIGH", "2026-02-14"));
        tasks.add(new Task(5L, "Team Meeting", "Attend weekly team meeting", false, "MEDIUM", "2026-02-11"));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        return tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .map(task -> ResponseEntity.ok((Object) task))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with ID " + taskId + " not found"));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> results = tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> results = tasks.stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setTaskId(nextId++);
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        return tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setCompleted(updatedTask.isCompleted());
                    task.setPriority(updatedTask.getPriority());
                    task.setDueDate(updatedTask.getDueDate());
                    return ResponseEntity.ok((Object) task);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with ID " + taskId + " not found"));
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<?> markTaskAsCompleted(@PathVariable Long taskId) {
        return tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .map(task -> {
                    task.setCompleted(true);
                    return ResponseEntity.ok((Object) task);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with ID " + taskId + " not found"));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        boolean removed = tasks.removeIf(task -> task.getTaskId().equals(taskId));
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Task with ID " + taskId + " not found");
    }
}
