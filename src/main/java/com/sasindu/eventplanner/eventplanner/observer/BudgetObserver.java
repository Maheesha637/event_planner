package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer for budget/expense related notifications.
 */
@Component
public class BudgetObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(BudgetObserver.class);

    @Override
    public void update(Notification notification) {
        if (!ObserverType.BUDGET.equals(notification.getType())) return;
        logger.info("[BudgetObserver] Received notification for event {}: {}", notification.getEventId(), notification.getMessage());
        // e.g. alert when budget limit is near/exceeded
    }
}