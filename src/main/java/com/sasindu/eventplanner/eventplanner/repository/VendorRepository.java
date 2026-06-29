package com.sasindu.eventplanner.eventplanner.repository;

import com.sasindu.eventplanner.eventplanner.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    List<Vendor> findByUserUserIDAndEventEventID(Integer userId, Integer eventId);
}