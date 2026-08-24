package com.nt.Designpattren.Designpattren.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.Designpattren.Designpattren.model.Election;

public interface ElectionRepository
        extends JpaRepository<Election, Long> {

    // Keep the existing method
    Optional<Election> findByElectionType(
            String electionType
    );

    // Add this method for ElectionService
    Optional<Election> findByElectionTypeIgnoreCase(
            String electionType
    );
}