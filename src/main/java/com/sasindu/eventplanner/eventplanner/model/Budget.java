package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Budget")
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BudgetID")
    private Integer budgetID;

    @Column(name = "Theme")
    private String theme;

    @Column(name = "TotalBudget")
    private Double totalBudget;

    @Column(name = "Achievement")
    private Double achievement;

    @OneToOne
    @JoinColumn(name = "EventID", referencedColumnName = "EventID")
    private Event event;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<Expense> expenses;
}