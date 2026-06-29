package com.sasindu.eventplanner.eventplanner.repository;

import com.sasindu.eventplanner.eventplanner.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    boolean existsByFirstNameAndLastNameAndEventEventID(String firstName, String lastName, Integer eventID);
    List<Guest> findByEventEventID(Integer eventId);
}