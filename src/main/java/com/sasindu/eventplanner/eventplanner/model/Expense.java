package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Expense")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExpenseID")
    private Integer expenseID;

    @Column(name = "Description")
    private String description;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "Date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "BudgetID", referencedColumnName = "BudgetID")
    private Budget budget;
}