package com.sasindu.eventplanner.eventplanner.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Central notification subject (thread-safe).
 * Register observers and publish Notification instances.
 */
@Service
public class NotificationService implements Subject {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // thread-safe set of observers
    private final Set<Observer> observers = ConcurrentHashMap.newKeySet();

    @Override
    public void register(Observer observer) {
        if (observer != null) {
            observers.add(observer);
            logger.debug("Registered observer: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void unregister(Observer observer) {
        if (observer != null) {
            observers.remove(observer);
            logger.debug("Unregistered observer: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void publish(Notification notification) {
        if (notification == null) return;
        logger.info("Publishing notification: {}", notification);
        // iterate over a snapshot (set is thread-safe) and notify each observer
        for (Observer o : observers) {
            try {
                o.update(notification);
            } catch (Exception ex) {
                // Observers should handle exceptions but we guard here to keep other observers receiving events
                logger.error("Observer {} threw exception while handling notification {}: {}", o.getClass().getSimpleName(), notification, ex.getMessage(), ex);
            }
        }
    }

    // Convenience helper to publish simple notifications without building map
    public void publish(String type, String message, Integer eventId) {
        publish(new Notification(type, message, eventId, Map.of()));
    }

    // Convenience helper with metadata
    public void publish(String type, String message, Integer eventId, Map<String, Object> metadata) {
        publish(new Notification(type, message, eventId, metadata));
    }
}