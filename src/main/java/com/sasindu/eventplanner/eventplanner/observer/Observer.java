package com.sasindu.eventplanner.eventplanner.observer;

/**
 * Observer contract for receiving notifications.
 */
public interface Observer {
    /**
     * Called by Subject when a notification is published.
     */
    void update(Notification notification);
}