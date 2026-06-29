package com.sasindu.eventplanner.eventplanner;

import com.sasindu.eventplanner.eventplanner.service.AdminSetupService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EventPlannerApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EventPlannerApplication.class, args);
        // Create admin user if not exists
        context.getBean(AdminSetupService.class).createAdminIfNotExists();
    }
}