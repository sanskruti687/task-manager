-- Create database
CREATE DATABASE IF NOT EXISTS taskmanager_db;
USE taskmanager_db;

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Create index for better performance
CREATE INDEX idx_completed ON tasks(completed);
CREATE INDEX idx_created_at ON tasks(created_at);

-- Insert sample data (optional)
INSERT INTO tasks (title, description, completed) VALUES
                                                      ('Setup Development Environment', 'Install Java, Maven, MySQL and setup the project', true),
                                                      ('Create Database Schema', 'Design and create the database structure for tasks', true),
                                                      ('Implement REST API', 'Create Spring Boot controllers for CRUD operations', false),
                                                      ('Design Frontend UI', 'Create responsive HTML/CSS interface', false),
                                                      ('Add Task Filtering', 'Implement filter functionality for completed/pending tasks', false),
                                                      ('Deploy to Production', 'Deploy the application to cloud platform', false);

-- Query to check all tables
SHOW TABLES;

-- Query to describe tasks table structure
DESCRIBE tasks;

-- Useful queries for testing:

-- Get all tasks
SELECT * FROM tasks ORDER BY created_at DESC;

-- Get completed tasks
SELECT * FROM tasks WHERE completed = TRUE ORDER BY created_at DESC;

-- Get pending tasks
SELECT * FROM tasks WHERE completed = FALSE ORDER BY created_at DESC;

-- Count total tasks
SELECT COUNT(*) as total_tasks FROM tasks;

-- Count completed tasks
SELECT COUNT(*) as completed_tasks FROM tasks WHERE completed = TRUE;

-- Count pending tasks
SELECT COUNT(*) as pending_tasks FROM tasks WHERE completed = FALSE;

-- Update task status
-- UPDATE tasks SET completed = TRUE WHERE id = 1;

-- Delete task
-- DELETE FROM tasks WHERE id = 1;

-- Search tasks by title
-- SELECT * FROM tasks WHERE title LIKE '%search_term%' ORDER BY created_at DESC;