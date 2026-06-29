package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.repository.EventRepository;
import com.sasindu.eventplanner.eventplanner.observer.NotificationService;
import com.sasindu.eventplanner.eventplanner.observer.ObserverType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByUser(User user) {
        return eventRepository.findByUserUserID(user.getUserID());
    }

    public Event getEventById(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    public String getEventNameById(Integer eventId) {
        Event event = getEventById(eventId);
        return event.getName();
    }

    public Event createEvent(Event event) {
        if (event.getName() == null || event.getName().isEmpty() || event.getDate() == null) {
            throw new IllegalArgumentException("Missing fields - Please enter name and date");
        }
        if (eventRepository.existsByNameAndDate(event.getName(), event.getDate())) {
            throw new IllegalArgumentException("Duplicate event name/date - Please choose different");
        }
        Event saved = eventRepository.save(event);

        try {
            notificationService.publish(ObserverType.EVENT, "Event created: " + saved.getName(), saved.getEventID());
        } catch (Exception ex) {}

        return saved;
    }

    public Event updateEvent(Integer eventId, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (!existingEvent.getUser().getUserID().equals(updatedEvent.getUser().getUserID())) {
            throw new IllegalArgumentException("You do not have permission to update this event");
        }
        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDate(updatedEvent.getDate());
        existingEvent.setType(updatedEvent.getType());
        existingEvent.setStatus(updatedEvent.getStatus());
        existingEvent.setLocation(updatedEvent.getLocation());
        Event saved = eventRepository.save(existingEvent);

        try {
            notificationService.publish(ObserverType.EVENT, "Event updated: " + saved.getName(), saved.getEventID());
        } catch (Exception ex) {}

        return saved;
    }

    public void deleteEvent(Integer eventId, User user) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (!event.getUser().getUserID().equals(user.getUserID())) {
            throw new IllegalArgumentException("You do not have permission to delete this event");
        }
        eventRepository.deleteById(eventId);

        try {
            notificationService.publish(ObserverType.EVENT, "Event deleted (id=" + eventId + ")", eventId);
        } catch (Exception ex) {}
    }
}