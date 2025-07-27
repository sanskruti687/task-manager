package com.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Repository Interface
@Repository
interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedOrderByCreatedAtDesc(Boolean completed);
    List<Task> findAllByOrderByCreatedAtDesc();
}

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Allow all origins for simplicity
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Get all tasks
    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) String status) {
        if (status == null || status.equals("all")) {
            return taskRepository.findAllByOrderByCreatedAtDesc();
        } else if (status.equals("completed")) {
            return taskRepository.findByCompletedOrderByCreatedAtDesc(true);
        } else if (status.equals("pending")) {
            return taskRepository.findByCompletedOrderByCreatedAtDesc(false);
        }
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }

    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Create new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        // Ensure completed is false for new tasks
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setCompleted(taskDetails.getCompleted());
            return ResponseEntity.ok(taskRepository.save(task));
        }
        return ResponseEntity.notFound().build();
    }

    // Toggle task completion status
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTaskStatus(@PathVariable Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.getCompleted());
            return ResponseEntity.ok(taskRepository.save(task));
        }
        return ResponseEntity.notFound().build();
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Get task statistics
    @GetMapping("/stats")
    public TaskStats getTaskStats() {
        List<Task> allTasks = taskRepository.findAll();
        long totalTasks = allTasks.size();
        long completedTasks = allTasks.stream().mapToLong(task -> task.getCompleted() ? 1 : 0).sum();
        long pendingTasks = totalTasks - completedTasks;

        return new TaskStats(totalTasks, completedTasks, pendingTasks);
    }

    // Inner class for task statistics
    public static class TaskStats {
        private long total;
        private long completed;
        private long pending;

        public TaskStats(long total, long completed, long pending) {
            this.total = total;
            this.completed = completed;
            this.pending = pending;
        }

        // Getters
        public long getTotal() { return total; }
        public long getCompleted() { return completed; }
        public long getPending() { return pending; }

        // Setters
        public void setTotal(long total) { this.total = total; }
        public void setCompleted(long completed) { this.completed = completed; }
        public void setPending(long pending) { this.pending = pending; }
    }
}