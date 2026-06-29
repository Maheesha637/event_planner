package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.Task;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.service.TaskService;
import com.sasindu.eventplanner.eventplanner.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private EventService eventService;

    @GetMapping("/events/{eventId}/tasks")
    public String showTaskList(@PathVariable Integer eventId, Model model, @AuthenticationPrincipal User user) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("tasks", taskService.getTasksByEventId(eventId));
        model.addAttribute("task", new Task());
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("user", user);
        return "tasks";
    }

    @PostMapping("/events/{eventId}/tasks")
    public String addTask(@ModelAttribute Task task, @PathVariable Integer eventId, Model model) {
        try {
            Event event = eventService.getEventById(eventId);
            task.setEvent(event);
            taskService.addTask(task);
            return "redirect:/events/" + eventId + "/tasks";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("eventId", eventId);
            model.addAttribute("eventName", eventService.getEventNameById(eventId));
            return "tasks";
        }
    }

    // Update status only (table inline)
    @PostMapping("/events/{eventId}/tasks/{taskId}/update-status")
    public String updateTaskStatus(@PathVariable Integer eventId,
                                   @PathVariable Integer taskId,
                                   @RequestParam String status) {
        taskService.updateTaskStatus(taskId, status);
        return "redirect:/events/" + eventId + "/tasks";
    }

    // Delete task (table inline)
    @PostMapping("/events/{eventId}/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable Integer eventId,
                             @PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/events/" + eventId + "/tasks";
    }

    // Show edit details page
    @GetMapping("/events/{eventId}/tasks/{taskId}/edit")
    public String showEditTaskPage(@PathVariable Integer eventId,
                                   @PathVariable Integer taskId,
                                   Model model) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("eventId", eventId);
        return "edit-task";
    }

    // Update details from edit-task.html
    @PostMapping("/events/{eventId}/tasks/{taskId}/update")
    public String updateTaskDetails(@PathVariable Integer eventId,
                                    @PathVariable Integer taskId,
                                    @ModelAttribute Task task) {
        taskService.updateTaskDetails(taskId, task);
        return "redirect:/events/" + eventId + "/tasks";
    }
}