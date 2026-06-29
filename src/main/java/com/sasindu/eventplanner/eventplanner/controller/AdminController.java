package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.service.AdminService;
import com.sasindu.eventplanner.eventplanner.service.BackupService;
import com.sasindu.eventplanner.eventplanner.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BackupService backupService;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAdminDashboard(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("user", user);

        // Add events so admin can export guest lists for any event
        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);

        return "admin-dashboard";
    }

    @PostMapping("/admin/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam String password,
                             Model model) {
        try {
            adminService.createUser(firstName, lastName, email, password);
            model.addAttribute("success", "User created successfully");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create user");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/deleteUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam Integer userID, Model model) {
        try {
            adminService.deleteUser(userID);
            model.addAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete user");
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Stream a full database backup as a ZIP of CSVs. Admin clicks the "Download Backup" link and the browser downloads the zip.
     *
     * NOTE: switched to GET to avoid the BadRequest conversion error you were seeing caused by a "null" param being posted.
     * The endpoint is still protected for admins only.
     */
    @GetMapping("/admin/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public void backupData(HttpServletResponse response, @AuthenticationPrincipal User user) throws IOException {
        try {
            // This streams the zip file back to the client as the response body
            backupService.streamFullDatabaseBackup(response);
        } catch (Exception e) {
            // On error, return a sensible status and log/propagate message (avoid exposing sensitive internals)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            response.getWriter().write("Failed to create backup: " + e.getMessage());
            response.getWriter().flush();
        }
    }
}