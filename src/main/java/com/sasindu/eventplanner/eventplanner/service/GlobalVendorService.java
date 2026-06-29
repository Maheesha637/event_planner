package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.GlobalVendor;
import com.sasindu.eventplanner.eventplanner.repository.GlobalVendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalVendorService {
    @Autowired
    private GlobalVendorRepository globalVendorRepository;

    public List<GlobalVendor> getAllGlobalVendors() {
        return globalVendorRepository.findAll();
    }

    public GlobalVendor addGlobalVendor(GlobalVendor vendor) {
        if (vendor.getName() == null || vendor.getContactDetails() == null) {
            throw new IllegalArgumentException("Name and Contact Details are required");
        }
        return globalVendorRepository.save(vendor);
    }

    public void deleteGlobalVendor(Integer vendorId) {
        globalVendorRepository.deleteById(vendorId);
    }
}