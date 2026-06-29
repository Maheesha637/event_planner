package com.sasindu.eventplanner.eventplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // The name of your Thymeleaf template (access-denied.html)
    }
}