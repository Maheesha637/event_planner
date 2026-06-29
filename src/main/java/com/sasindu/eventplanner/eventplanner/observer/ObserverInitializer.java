package com.sasindu.eventplanner.eventplanner.observer;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Registers built-in observers with the NotificationService at startup.
 * Keeps the wiring and registration contained so you don't need to modify existing code to enable notifications.
 */
@Component
public class ObserverInitializer {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GuestObserver guestObserver;

    @Autowired
    private TaskObserver taskObserver;

    @Autowired
    private VendorObserver vendorObserver;

    @Autowired
    private BudgetObserver budgetObserver;

    @Autowired
    private ContractObserver contractObserver;

    @Autowired
    private EventObserver eventObserver;

    @Autowired
    private AdminObserver adminObserver;

    @PostConstruct
    public void init() {
        // Register all built-in observers. You can later unregister or register additional observers dynamically.
        notificationService.register(guestObserver);
        notificationService.register(taskObserver);
        notificationService.register(vendorObserver);
        notificationService.register(budgetObserver);
        notificationService.register(contractObserver);
        notificationService.register(eventObserver);
        notificationService.register(adminObserver);
    }
}