package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.model.Budget;
import com.sasindu.eventplanner.eventplanner.model.Event;
import com.sasindu.eventplanner.eventplanner.model.Expense;
import com.sasindu.eventplanner.eventplanner.model.User;
import com.sasindu.eventplanner.eventplanner.service.BudgetService;
import com.sasindu.eventplanner.eventplanner.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @Autowired
    private EventService eventService;

    @GetMapping("/events/{eventId}/budget")
    public String showBudget(@PathVariable Integer eventId, Model model, @AuthenticationPrincipal User user) {
        Event event = eventService.getEventById(eventId);
        Budget existingBudget = budgetService.getBudgetByEventId(eventId);
        boolean hasBudget = existingBudget != null;

        model.addAttribute("budget", hasBudget ? existingBudget : new Budget());
        model.addAttribute("expense", new Expense());
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("hasBudget", hasBudget);
        model.addAttribute("user", user);

        if (hasBudget) {
            model.addAttribute("expenses", budgetService.getExpensesByBudgetId(existingBudget.getBudgetID()));
        }

        return "budget";
    }

    @PostMapping("/events/{eventId}/budget")
    public String createOrUpdateBudget(@ModelAttribute("budget") Budget budget, @PathVariable Integer eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        budget.setEvent(event);
        try {
            Budget existingBudget = budgetService.getBudgetByEventId(eventId);
            if (existingBudget != null) {
                budget.setBudgetID(existingBudget.getBudgetID());
                budget.setAchievement(existingBudget.getAchievement());
                budgetService.updateBudget(budget);
                model.addAttribute("success", "Budget updated successfully");
            } else {
                budget.setAchievement(0.0);
                budgetService.createBudget(budget);
                model.addAttribute("success", "Budget created successfully");
            }
            return "redirect:/events/" + eventId + "/budget";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("eventName", event.getName());
            return "budget";
        }
    }

    @PostMapping("/events/{eventId}/budget/expense")
    public String addExpense(@ModelAttribute("expense") Expense expense, @PathVariable Integer eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        Budget budget = budgetService.getBudgetByEventId(eventId);
        expense.setBudget(budget);
        try {
            budgetService.addExpense(expense);
            model.addAttribute("success", "Expense added successfully");
            return "redirect:/events/" + eventId + "/budget";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("eventName", event.getName());
            return "budget";
        }
    }

    // Edit expense form
    @GetMapping("/events/{eventId}/budget/expense/{expenseId}/edit")
    public String showEditExpensePage(@PathVariable Integer eventId,
                                      @PathVariable Integer expenseId,
                                      Model model) {
        Expense expense = budgetService.getExpenseById(expenseId);
        model.addAttribute("expense", expense);
        model.addAttribute("eventId", eventId);
        return "edit-expense";
    }

    // Update expense
    @PostMapping("/events/{eventId}/budget/expense/{expenseId}/update")
    public String updateExpense(@PathVariable Integer eventId,
                                @PathVariable Integer expenseId,
                                @ModelAttribute Expense expense) {
        budgetService.updateExpense(expenseId, expense);
        return "redirect:/events/" + eventId + "/budget";
    }

    // Delete expense
    @PostMapping("/events/{eventId}/budget/expense/{expenseId}/delete")
    public String deleteExpense(@PathVariable Integer eventId,
                                @PathVariable Integer expenseId) {
        budgetService.deleteExpense(expenseId);
        return "redirect:/events/" + eventId + "/budget";
    }
}