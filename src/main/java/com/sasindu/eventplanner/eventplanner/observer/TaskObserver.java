package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for task-related notifications.
 */
@Component
public class TaskObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(TaskObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.TASK.equals(notification.getType())) return;
        logger.info("[TaskObserver] Received notification for event {}: {}", notification.getEventId(), notification.getMessage());
        // e.g. escalate overdue tasks, notify assigned users, etc.
    }
}