package com.sasindu.eventplanner.eventplanner.repository;

import com.sasindu.eventplanner.eventplanner.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByBudgetBudgetID(Integer budgetId);
}