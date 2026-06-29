package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.Budget;
import com.sasindu.eventplanner.eventplanner.model.Expense;
import com.sasindu.eventplanner.eventplanner.repository.BudgetRepository;
import com.sasindu.eventplanner.eventplanner.repository.ExpenseRepository;
import com.sasindu.eventplanner.eventplanner.observer.NotificationService;
import com.sasindu.eventplanner.eventplanner.observer.ObserverType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private NotificationService notificationService;

    public Budget getBudgetByEventId(Integer eventId) {
        return budgetRepository.findByEventEventID(eventId);
    }

    public List<Expense> getExpensesByBudgetId(Integer budgetId) {
        return expenseRepository.findByBudgetBudgetID(budgetId);
    }

    public Budget createBudget(Budget budget) {
        if (budget.getTheme() == null || budget.getTotalBudget() == null || budget.getEvent() == null) {
            throw new IllegalArgumentException("Missing fields - Theme and Total Budget are required");
        }
        Budget saved = budgetRepository.save(budget);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.BUDGET, "Budget created (theme=" + saved.getTheme() + ", total=" + saved.getTotalBudget() + ")", eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public Budget updateBudget(Budget budget) {
        if (budget.getTheme() == null || budget.getTotalBudget() == null || budget.getEvent() == null) {
            throw new IllegalArgumentException("Missing fields - Theme and Total Budget are required");
        }
        if (budget.getTotalBudget() < budget.getAchievement()) {
            throw new IllegalArgumentException("Total budget cannot be less than current expenses");
        }
        Budget saved = budgetRepository.save(budget);

        try {
            Integer eventId = saved.getEvent() != null ? saved.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.BUDGET, "Budget updated (id=" + saved.getBudgetID() + ")", eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public Expense addExpense(Expense expense) {
        if (expense.getDescription() == null || expense.getAmount() == null ||
                expense.getDate() == null || expense.getBudget() == null) {
            throw new IllegalArgumentException("Missing fields - Description, Amount, and Date are required");
        }

        Budget budget = expense.getBudget();
        double newTotal = budget.getAchievement() + expense.getAmount();

        if (newTotal > budget.getTotalBudget()) {
            throw new IllegalArgumentException("Budget exceeded - Cannot add this expense");
        }

        budget.setAchievement(newTotal);
        budgetRepository.save(budget);
        Expense saved = expenseRepository.save(expense);

        try {
            Integer eventId = budget.getEvent() != null ? budget.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.BUDGET, "Expense added: " + saved.getDescription() + " ($" + saved.getAmount() + ")", eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public Expense getExpenseById(Integer expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
    }

    public Expense updateExpense(Integer expenseId, Expense newExpenseData) {
        Expense existingExpense = getExpenseById(expenseId);
        Budget budget = existingExpense.getBudget();

        // Remove old amount, add new amount to achievement
        double oldAchievement = budget.getAchievement() - existingExpense.getAmount();
        double newAchievement = oldAchievement + newExpenseData.getAmount();

        if (newAchievement > budget.getTotalBudget()) {
            throw new IllegalArgumentException("Budget exceeded - Cannot update this expense");
        }

        budget.setAchievement(newAchievement);
        budgetRepository.save(budget);

        existingExpense.setDescription(newExpenseData.getDescription());
        existingExpense.setAmount(newExpenseData.getAmount());
        existingExpense.setDate(newExpenseData.getDate());
        Expense saved = expenseRepository.save(existingExpense);

        try {
            Integer eventId = budget.getEvent() != null ? budget.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.BUDGET, "Expense updated (id=" + saved.getExpenseID() + ")", eventId);
        } catch (Exception ex) {}

        return saved;
    }

    public void deleteExpense(Integer expenseId) {
        Expense expense = getExpenseById(expenseId);
        Budget budget = expense.getBudget();

        // Subtract expense amount from achievement
        budget.setAchievement(budget.getAchievement() - expense.getAmount());
        budgetRepository.save(budget);

        expenseRepository.deleteById(expenseId);

        try {
            Integer eventId = budget.getEvent() != null ? budget.getEvent().getEventID() : null;
            notificationService.publish(ObserverType.BUDGET, "Expense deleted (id=" + expenseId + ")", eventId);
        } catch (Exception ex) {}
    }
}