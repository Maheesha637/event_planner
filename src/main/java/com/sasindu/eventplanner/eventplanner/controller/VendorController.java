package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.model.Vendor;
import com.sasindu.eventplanner.eventplanner.model.Contract;
import com.sasindu.eventplanner.eventplanner.service.EventService;
import com.sasindu.eventplanner.eventplanner.service.VendorService;
import com.sasindu.eventplanner.eventplanner.service.GlobalVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class VendorController {

    @Autowired
    private EventService eventService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private GlobalVendorService globalVendorService;

    @GetMapping("/{eventId}/vendors")
    public String showVendorManagement(@PathVariable Integer eventId,
                                       @AuthenticationPrincipal User user,
                                       Model model) {
        String eventName = eventService.getEventNameById(eventId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", eventName);
        model.addAttribute("vendor", new Vendor());
        model.addAttribute("vendors", vendorService.findByUserUserIDAndEventEventID(user.getUserID(), eventId));
        model.addAttribute("globalVendors", globalVendorService.getAllGlobalVendors()); // <-- added
        model.addAttribute("user", user);
        return "vendors";
    }

    @PostMapping("/{eventId}/vendors")
    public String addVendor(@ModelAttribute Vendor vendor,
                            @PathVariable Integer eventId,
                            @AuthenticationPrincipal User user,
                            Model model) {
        try {
            Event event = eventService.getEventById(eventId);
            vendor.setEvent(event);
            vendor.setUser(user);
            vendorService.addVendor(vendor);
            return "redirect:/events/" + eventId + "/vendors";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "vendors";
        }
    }

    // Add a GlobalVendor into this event's vendors (creates a Vendor copied from GlobalVendor)
    @PostMapping("/{eventId}/vendors/global/{globalVendorId}/add")
    public String addGlobalVendorToEvent(@PathVariable Integer eventId,
                                         @PathVariable Integer globalVendorId,
                                         @AuthenticationPrincipal User user,
                                         Model model) {
        try {
            Event event = eventService.getEventById(eventId);
            vendorService.addVendorFromGlobal(globalVendorId, event, user);
            return "redirect:/events/" + eventId + "/vendors";
        } catch (IllegalArgumentException e) {
            // show error on the page
            model.addAttribute("error", e.getMessage());
            // reload lists
            String eventName = eventService.getEventNameById(eventId);
            model.addAttribute("eventId", eventId);
            model.addAttribute("eventName", eventName);
            model.addAttribute("vendor", new Vendor());
            model.addAttribute("vendors", vendorService.findByUserUserIDAndEventEventID(user.getUserID(), eventId));
            model.addAttribute("globalVendors", globalVendorService.getAllGlobalVendors());
            model.addAttribute("user", user);
            return "vendors";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add global vendor: " + e.getMessage());
            String eventName = eventService.getEventNameById(eventId);
            model.addAttribute("eventId", eventId);
            model.addAttribute("eventName", eventName);
            model.addAttribute("vendor", new Vendor());
            model.addAttribute("vendors", vendorService.findByUserUserIDAndEventEventID(user.getUserID(), eventId));
            model.addAttribute("globalVendors", globalVendorService.getAllGlobalVendors());
            model.addAttribute("user", user);
            return "vendors";
        }
    }

    // Show contract creation page
    @GetMapping("/{eventId}/vendors/{vendorId}/contract/create")
    public String showCreateContractPage(@PathVariable Integer eventId,
                                         @PathVariable Integer vendorId,
                                         Model model) {
        model.addAttribute("eventId", eventId);
        model.addAttribute("vendorId", vendorId);
        model.addAttribute("contract", new Contract());
        return "create-contract";
    }

    // Handle contract creation
    @PostMapping("/{eventId}/vendors/{vendorId}/contract/create")
    public String createContract(@ModelAttribute Contract contract,
                                 @PathVariable Integer eventId,
                                 @PathVariable Integer vendorId) {
        vendorService.addContract(contract, vendorId);
        return "redirect:/events/" + eventId + "/vendors";
    }

    // Edit vendor
    @GetMapping("/{eventId}/vendors/{vendorId}/edit")
    public String showEditVendorPage(@PathVariable Integer eventId,
                                     @PathVariable Integer vendorId,
                                     Model model) {
        Vendor vendor = vendorService.getVendorById(vendorId);
        model.addAttribute("vendor", vendor);
        model.addAttribute("eventId", eventId);
        return "edit-vendor";
    }

    @PostMapping("/{eventId}/vendors/{vendorId}/update")
    public String updateVendor(@PathVariable Integer eventId,
                               @PathVariable Integer vendorId,
                               @ModelAttribute Vendor vendor) {
        vendorService.updateVendorDetails(vendorId, vendor);
        return "redirect:/events/" + eventId + "/vendors";
    }

    // Remove vendor
    @PostMapping("/{eventId}/vendors/{vendorId}/remove")
    public String removeVendor(@PathVariable Integer eventId,
                               @PathVariable Integer vendorId) {
        vendorService.removeVendor(vendorId);
        return "redirect:/events/" + eventId + "/vendors";
    }
}