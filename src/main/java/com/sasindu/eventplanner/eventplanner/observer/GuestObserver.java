package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for guest-related notifications.
 * Extend behavior (persist, send email, push websocket) here later.
 */
@Component
public class GuestObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(GuestObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.GUEST.equals(notification.getType())) return;
        // Default behaviour: log. Replace/extend with your preferred notification delivery.
        logger.info("[GuestObserver] Received notification for event {}: {}", notification.getEventId(), notification.getMessage());
        // Additional handling: enrich or persist to DB, send email, etc.
    }
}