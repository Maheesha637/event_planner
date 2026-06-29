package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for contract/payment related notifications.
 */
@Component
public class ContractObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(ContractObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.CONTRACT.equals(notification.getType())) return;
        logger.info("[ContractObserver] Received notification for event {}: {}", notification.getEventId(), notification.getMessage());
        // e.g. send payment reminders, update contract dashboards
    }
}