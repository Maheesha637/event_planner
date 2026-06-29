package com.sasindu.eventplanner.eventplanner.observer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Simple notification model used by the observer system.
 */
public class Notification {
    private final String type;         // e.g. "GUEST", "TASK", "VENDOR", ...
    private final String message;      // human readable message
    private final Integer eventId;     // optional event id related to the notification
    private final LocalDateTime createdAt;
    private final Map<String, Object> metadata;

    public Notification(String type, String message, Integer eventId, Map<String, Object> metadata) {
        this.type = type;
        this.message = message;
        this.eventId = eventId;
        this.createdAt = LocalDateTime.now();
        this.metadata = metadata == null ? Collections.emptyMap() : Collections.unmodifiableMap(metadata);
    }

    public String getType() { return type; }
    public String getMessage() { return message; }
    public Integer getEventId() { return eventId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Map<String, Object> getMetadata() { return metadata; }

    @Override
    public String toString() {
        return "Notification{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", eventId=" + eventId +
                ", createdAt=" + createdAt +
                ", metadata=" + metadata +
                '}';
    }
}