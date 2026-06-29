package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for general event-level notifications.
 */
@Component
public class EventObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(EventObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.EVENT.equals(notification.getType())) return;
        logger.info("[EventObserver] Received notification for event {}: {}", notification.getEventId(), notification.getMessage());
        // e.g. notify event owner when important changes happen
    }
}