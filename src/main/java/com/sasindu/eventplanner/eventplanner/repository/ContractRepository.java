package com.sasindu.eventplanner.eventplanner.repository;

import com.sasindu.eventplanner.eventplanner.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
}