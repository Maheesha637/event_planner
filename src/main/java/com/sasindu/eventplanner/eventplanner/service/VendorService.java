package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Contract;
import com.sasindu.eventplanner.eventplanner.model.GlobalVendor;
import com.sasindu.eventplanner.eventplanner.model.Vendor;
import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.repository.ContractRepository;
import com.sasindu.eventplanner.eventplanner.repository.GlobalVendorRepository;
import com.sasindu.eventplanner.eventplanner.repository.VendorRepository;
import com.sasindu.eventplanner.eventplanner.observer.NotificationService;
import com.sasindu.eventplanner.eventplanner.observer.ObserverType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private GlobalVendorRepository globalVendorRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Vendor> findByUserUserIDAndEventEventID(Integer userId, Integer eventId){
        return vendorRepository.findByUserUserIDAndEventEventID(userId, eventId);
    }

    public Vendor addVendor(Vendor vendor) {
        if (vendor.getName() == null || vendor.getContactDetails() == null) {
            throw new IllegalArgumentException("Name and Contact Details are required");
        }
        Vendor saved = vendorRepository.save(vendor);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.VENDOR, "Vendor added: " + saved.getName(), eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public Vendor getVendorById(Integer vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
    }

    public Vendor updateVendorDetails(Integer vendorId, Vendor updatedVendor) {
        Vendor vendor = getVendorById(vendorId);
        vendor.setName(updatedVendor.getName());
        vendor.setCategory(updatedVendor.getCategory());
        vendor.setDescription(updatedVendor.getDescription());
        vendor.setContactDetails(updatedVendor.getContactDetails());
        vendor.setRating(updatedVendor.getRating());
        Vendor saved = vendorRepository.save(vendor);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.VENDOR, "Vendor updated: " + saved.getName(), eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public void removeVendor(Integer vendorId) {
        Vendor v = vendorRepository.findById(vendorId).orElse(null);
        Integer eventId = v != null && v.getEvent() != null ? v.getEvent().getEventID() : null;

        vendorRepository.deleteById(vendorId);

        try {
            notificationService.publish(ObserverType.VENDOR, "Vendor removed (id=" + vendorId + ")", eventId);
        } catch (Exception ex) {}
    }

    // Contract functions
    public Contract addContract(Contract contract, Integer vendorId) {
        Vendor vendor = getVendorById(vendorId);
        contract.setVendor(vendor);
        Contract saved = contractRepository.save(contract);

        try {
            Integer eventId = vendor.getEvent() != null ? vendor.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.CONTRACT, "Contract created for vendor: " + vendor.getName() + " (amount=" + saved.getPaymentAmount() + ")", eventId);
        } catch (Exception ex) {}

        return saved;
    }

    /**
     * Create a Vendor record for this event by copying data from a GlobalVendor.
     * Associates the created vendor with the given event and user.
     */
    public Vendor addVendorFromGlobal(Integer globalVendorId, Event event, User user) {
        GlobalVendor gv = globalVendorRepository.findById(globalVendorId)
                .orElseThrow(() -> new IllegalArgumentException("Global vendor not found"));

        // Prevent duplicates for the same user/event by name + contact details
        List<Vendor> existing = vendorRepository.findByUserUserIDAndEventEventID(user.getUserID(), event.getEventID());
        for (Vendor v : existing) {
            if (v.getName() != null && gv.getName() != null && v.getName().equalsIgnoreCase(gv.getName())
                    && ((v.getContactDetails() == null && gv.getContactDetails() == null) ||
                    (v.getContactDetails() != null && v.getContactDetails().equalsIgnoreCase(gv.getContactDetails())))) {
                throw new IllegalArgumentException("Vendor already added to this event");
            }
        }

        Vendor vendor = new Vendor();
        vendor.setName(gv.getName());
        vendor.setCategory(gv.getCategory());
        vendor.setDescription(gv.getDescription());
        vendor.setContactDetails(gv.getContactDetails());
        vendor.setRating(gv.getRating());
        vendor.setEvent(event);
        vendor.setUser(user);
        Vendor saved = vendorRepository.save(vendor);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.VENDOR, "Global vendor added to event: " + saved.getName(), eventId);
        } catch (Exception ex) {}

        return saved;
    }
}