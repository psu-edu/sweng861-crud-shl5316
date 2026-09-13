package com.sweng861.agiletracker.controller;

import com.sweng861.agiletracker.exception.ResourceNotFoundException;
import com.sweng861.agiletracker.model.Task;
import com.sweng861.agiletracker.repository.TaskRespository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskRespository taskRespository;

    @Autowired
    public TaskController(TaskRespository taskRespository) {
        this.taskRespository = taskRespository;
    }

    // READ all tasks
    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskRespository.findAll();
    }

    // READ one task by ID
    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    // CREATE a new task
    @PostMapping("/tasks")
    public Task createTask(@Valid @RequestBody Task task) {
        return taskRespository.save(task);
    }

    // UPDATE an existing task
    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        Task existingTask = taskRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.isCompleted());

        return taskRespository.save(existingTask);
    }

    // DELETE a task
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRespository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id " + id);
        }
        taskRespository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}