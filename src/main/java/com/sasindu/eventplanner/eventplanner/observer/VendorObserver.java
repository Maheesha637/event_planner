package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for vendor-related notifications.
 */
@Component
public class VendorObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(VendorObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.VENDOR.equals(notification.getType())) return;
        logger.info("[VendorObserver] Received notification for event {}: {}", notification.getEventId(), notification.getMessage());
        // e.g. notify vendor contacts or update vendor dashboards
    }
}