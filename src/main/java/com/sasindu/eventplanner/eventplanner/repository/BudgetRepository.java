package com.sasindu.eventplanner.eventplanner.repository;

import com.sasindu.eventplanner.eventplanner.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    Budget findByEventEventID(Integer eventID);
}