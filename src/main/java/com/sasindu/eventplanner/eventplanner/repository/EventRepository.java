package com.sasindu.eventplanner.eventplanner.repository;

import com.sasindu.eventplanner.eventplanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    boolean existsByNameAndDate(String name, LocalDate date);
    List<Event> findByUserUserID(Integer userID);  // Add this method
}