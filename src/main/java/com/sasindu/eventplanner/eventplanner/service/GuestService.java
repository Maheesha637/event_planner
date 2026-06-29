package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Guest;
import com.sasindu.eventplanner.eventplanner.repository.GuestRepository;
import com.sasindu.eventplanner.eventplanner.observer.NotificationService;
import com.sasindu.eventplanner.eventplanner.observer.ObserverType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Guest> getGuestsByEventId(Integer eventId) {
        return guestRepository.findByEventEventID(eventId);
    }

    public Guest addGuest(Guest guest) {
        if (guest.getFirstName() == null || guest.getLastName() == null || guest.getEvent() == null) {
            throw new IllegalArgumentException("Missing fields - First Name, Last Name, and Event are required");
        }
        if (guestRepository.existsByFirstNameAndLastNameAndEventEventID(
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEvent().getEventID())) {
            throw new IllegalArgumentException("Duplicate guest - This guest already exists for the event");
        }
        Guest saved = guestRepository.save(guest);

        // Publish notification
        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.GUEST, "New guest added: " + saved.getFirstName() + " " + saved.getLastName(), eventId);
        } catch (Exception ex) {
            // swallow to avoid affecting normal flow; NotificationService already logs exceptions
        }

        return saved;
    }

    public Guest getGuestById(Integer guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));
    }

    public Guest updateGuestDetails(Integer guestId, Guest newGuestDetails) {
        Guest existingGuest = getGuestById(guestId);
        existingGuest.setFirstName(newGuestDetails.getFirstName());
        existingGuest.setLastName(newGuestDetails.getLastName());
        existingGuest.setContactDetails(newGuestDetails.getContactDetails());
        // event is not updated here
        Guest saved = guestRepository.save(existingGuest);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.GUEST, "Guest updated: " + saved.getFirstName() + " " + saved.getLastName(), eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public void removeGuest(Integer guestId) {
        // Try to find event id for notification
        Guest g = guestRepository.findById(guestId).orElse(null);
        Integer eventId = g != null && g.getEvent() != null ? g.getEvent().getEventID() : null;

        guestRepository.deleteById(guestId);

        try {
            notificationService.publish(ObserverType.GUEST, "Guest removed (id=" + guestId + ")", eventId);
        } catch (Exception ex) {}
    }
}