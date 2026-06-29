package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for admin-related notifications.
 * Extend this to send admin emails, create audit entries, or push system-wide alerts.
 */
@Component
public class AdminObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(AdminObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.ADMIN.equals(notification.getType())) return;
        logger.info("[AdminObserver] Received ADMIN notification: eventId={} message={}", notification.getEventId(), notification.getMessage());
        // TODO: persist, email, or notify admins via other channels
    }
}