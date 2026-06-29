package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Guest")
@Data
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GuestID")
    private Integer guestID;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "ContactDetails")
    private String contactDetails;

    @ManyToOne
    @JoinColumn(name = "EventID", referencedColumnName = "EventID")
    private Event event;
}