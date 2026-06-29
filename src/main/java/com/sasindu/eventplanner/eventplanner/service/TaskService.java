package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Task;
import com.sasindu.eventplanner.eventplanner.repository.TaskRepository;
import com.sasindu.eventplanner.eventplanner.observer.NotificationService;
import com.sasindu.eventplanner.eventplanner.observer.ObserverType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Task> getTasksByEventId(Integer eventId) {
        return taskRepository.findByEventEventID(eventId);
    }

    public Task addTask(Task task) {
        if (task.getDescription() == null || task.getDeadline() == null || task.getEvent() == null) {
            throw new IllegalArgumentException("Missing fields - Description, Deadline, and Event are required");
        }
        if (task.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Warning: Deadline is in the past");
        }
        Task saved = taskRepository.save(task);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.TASK, "New task added: " + saved.getDescription(), eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public void updateTaskStatus(Integer taskID, String status) {
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);

        try {
            Integer eventId = task.getEvent() != null ? task.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.TASK, "Task status updated (id=" + taskID + ") => " + status, eventId);
        } catch (Exception ex) {}
    }

    public void deleteTask(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found");
        }
        Task t = taskRepository.findById(taskId).orElse(null);
        Integer eventId = t != null && t.getEvent() != null ? t.getEvent().getEventID() : null;

        taskRepository.deleteById(taskId);

        try {
            notificationService.publish(ObserverType.TASK, "Task deleted (id=" + taskId + ")", eventId);
        } catch (Exception ex) {}
    }

    public Task getTaskById(Integer taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public Task updateTaskDetails(Integer taskId, Task updatedTask) {
        Task existingTask = getTaskById(taskId);
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDeadline(updatedTask.getDeadline());
        Task saved = taskRepository.save(existingTask);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.TASK, "Task updated: " + saved.getDescription(), eventId);
        } catch (Exception ex) {}

        return saved;
    }
}