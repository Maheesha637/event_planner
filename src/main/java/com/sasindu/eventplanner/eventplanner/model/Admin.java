package com.sasindu.eventplanner.eventplanner.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
public class Admin extends User {
    @Column(name = "privilege")    // Match the exact column name from DB
    private String privilege;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}