package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public String showEventForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("event", new Event());
        model.addAttribute("events", eventService.getEventsByUser(user));
        model.addAttribute("user", user);
        return "events";
    }

    @GetMapping("/{eventId}")
    public String redirectToEventDetails(@PathVariable Integer eventId) {
        return "redirect:/events/" + eventId + "/details";
    }

    @GetMapping("/{eventId}/details")
    public String showEventDetails(@PathVariable Integer eventId, Model model, @AuthenticationPrincipal User user) {
        Event event = eventService.getEventById(eventId);
        if (!event.getUser().getUserID().equals(user.getUserID())) {
            return "redirect:/events";
        }
        model.addAttribute("event", event);
        model.addAttribute("user", user);
        return "event-details";
    }

    @PostMapping("")
    public String createEvent(@Valid @ModelAttribute("event") Event event,
                              BindingResult result,
                              Model model,
                              @AuthenticationPrincipal User user) {
        event.setUser(user);
        if (result.hasErrors() || event.getName() == null || event.getName().isEmpty() || event.getDate() == null) {
            model.addAttribute("error", "Please enter a valid event name and date");
            model.addAttribute("events", eventService.getEventsByUser(user));
            return "events";
        }
        try {
            eventService.createEvent(event);
            model.addAttribute("success", "Event created successfully");
            return "redirect:/events";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("events", eventService.getEventsByUser(user));
            return "events";
        }
    }

    @GetMapping("/{eventId}/edit")
    public String showEditEventForm(@PathVariable Integer eventId, Model model, @AuthenticationPrincipal User user) {
        Event event = eventService.getEventById(eventId);
        if (!event.getUser().getUserID().equals(user.getUserID())) {
            return "redirect:/events";
        }
        model.addAttribute("event", event);
        return "edit-event";
    }

    @PostMapping("/{eventId}/update")
    public String updateEvent(@PathVariable Integer eventId,
                              @ModelAttribute("event") Event event,
                              Model model,
                              @AuthenticationPrincipal User user) {
        event.setUser(user);
        try {
            eventService.updateEvent(eventId, event);
            model.addAttribute("success", "Event updated successfully");
            return "redirect:/events";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("events", eventService.getEventsByUser(user));
            return "events";
        }
    }

    @PostMapping("/{eventId}/delete")
    public String deleteEvent(@PathVariable Integer eventId,
                              @AuthenticationPrincipal User user,
                              Model model) {
        try {
            eventService.deleteEvent(eventId, user);
            model.addAttribute("success", "Event deleted successfully");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }
}