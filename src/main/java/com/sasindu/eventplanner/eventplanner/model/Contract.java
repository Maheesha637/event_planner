package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Contract")
@Data
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractID")
    private Integer contractID;

    @Column(name = "Attribute")
    private String attribute;

    @Column(name = "PaymentAmount")
    private Double paymentAmount;

    @Column(name = "PaymentStatus")
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name = "VendorID", referencedColumnName = "VendorID")
    private Vendor vendor;
}