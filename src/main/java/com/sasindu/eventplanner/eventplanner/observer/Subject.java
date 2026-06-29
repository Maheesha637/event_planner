package com.sasindu.eventplanner.eventplanner.observer;

/**
 * Subject contract for registering/unregistering observers and publishing notifications.
 */
public interface Subject {
    void register(Observer observer);
    void unregister(Observer observer);
    void publish(Notification notification);
}