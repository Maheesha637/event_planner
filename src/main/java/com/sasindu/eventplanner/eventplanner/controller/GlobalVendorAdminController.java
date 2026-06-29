package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.GlobalVendor;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.service.GlobalVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/global-vendors")
@PreAuthorize("hasRole('ADMIN')")
public class GlobalVendorAdminController {

    @Autowired
    private GlobalVendorService globalVendorService;

    @GetMapping
    public String showGlobalVendors(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("globalVendors", globalVendorService.getAllGlobalVendors());
        model.addAttribute("globalVendor", new GlobalVendor());
        model.addAttribute("user", user);
        return "global-vendors"; // Create this Thymeleaf page
    }

    @PostMapping
    public String addGlobalVendor(@ModelAttribute GlobalVendor globalVendor, Model model) {
        try {
            globalVendorService.addGlobalVendor(globalVendor);
            model.addAttribute("success", "Vendor added successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add vendor: " + e.getMessage());
        }
        return "redirect:/admin/global-vendors";
    }

    @PostMapping("/{vendorId}/delete")
    public String deleteGlobalVendor(@PathVariable Integer vendorId) {
        globalVendorService.deleteGlobalVendor(vendorId);
        return "redirect:/admin/global-vendors";
    }
}