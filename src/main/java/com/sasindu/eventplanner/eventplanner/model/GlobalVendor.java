package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "GlobalVendor")
@Data
public class GlobalVendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GlobalVendorID")
    private Integer globalVendorID;

    @Column(name = "Name")
    private String name;

    @Column(name = "Category")
    private String category;

    @Column(name = "Description")
    private String description;

    @Column(name = "ContactDetails")
    private String contactDetails;

    @Column(name = "Rating")
    private Float rating;
}