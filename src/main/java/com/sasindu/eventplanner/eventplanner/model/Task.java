package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TaskID")
    private Integer taskID;

    @Column(name = "Description")
    private String description;

    @Column(name = "Deadline")
    private LocalDate deadline;

    @Column(name = "Status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "EventID", referencedColumnName = "EventID")
    private Event event;
}