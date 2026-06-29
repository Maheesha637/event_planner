package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.Guest;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.service.EventService;
import com.sasindu.eventplanner.eventplanner.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GuestController {
    @Autowired
    private GuestService guestService;

    @Autowired
    private EventService eventService;

    @GetMapping("/events/{eventId}/guests")
    public String showGuestList(@PathVariable Integer eventId, Model model, @AuthenticationPrincipal User user) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("guests", guestService.getGuestsByEventId(eventId));
        model.addAttribute("guest", new Guest());
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("user", user);
        return "guests";
    }

    @PostMapping("/events/{eventId}/guests")
    public String addGuest(@ModelAttribute("guest") Guest guest, @PathVariable Integer eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        guest.setEvent(event);
        try {
            guestService.addGuest(guest);
            return "redirect:/events/" + eventId + "/guests";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("eventId", eventId);
            model.addAttribute("eventName", event.getName());
            model.addAttribute("guests", guestService.getGuestsByEventId(eventId));
            return "guests";
        }
    }

    // Show edit guest page
    @GetMapping("/events/{eventId}/guests/{guestId}/edit")
    public String showEditGuestPage(@PathVariable Integer eventId,
                                    @PathVariable Integer guestId,
                                    Model model) {
        Guest guest = guestService.getGuestById(guestId);
        model.addAttribute("guest", guest);
        model.addAttribute("eventId", eventId);
        return "edit-guest";
    }

    // Update guest details
    @PostMapping("/events/{eventId}/guests/{guestId}/update")
    public String updateGuestDetails(@PathVariable Integer eventId,
                                     @PathVariable Integer guestId,
                                     @ModelAttribute Guest guest) {
        guestService.updateGuestDetails(guestId, guest);
        return "redirect:/events/" + eventId + "/guests";
    }

    @PostMapping("/events/{eventId}/guests/{guestId}/remove")
    public String removeGuest(@PathVariable Integer eventId, @PathVariable Integer guestId) {
        guestService.removeGuest(guestId);
        return "redirect:/events/" + eventId + "/guests";
    }
}