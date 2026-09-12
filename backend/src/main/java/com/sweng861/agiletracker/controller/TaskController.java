package com.sweng861.agiletracker.controller;

import com.sweng861.agiletracker.model.Task;
import com.sweng861.agiletracker.repository.TaskRespository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskRespository taskRespository;

    public TaskController(TaskRespository taskRespository) {
        this.taskRespository = taskRespository;
    }

    // READ all tasks
    @GetMapping("/tasks")
    public Object getAllTasks() {
        return taskRespository.findAll();
    }

    // READ one task by ID
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskRespository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE a new task
    @PostMapping("/tasks")
    public Task createTask(@Valid @RequestBody Task task) {
        return taskRespository.save(task);
    }

    // UPDATE an existing task
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        return taskRespository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setCompleted(taskDetails.isCompleted());
                    return taskRespository.save(task);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a task
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRespository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRespository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
