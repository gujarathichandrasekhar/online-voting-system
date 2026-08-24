package com.nt.Designpattren.Designpattren.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Designpattren.Designpattren.model.VoterAccount;

@Repository
public interface VoterAccountRepository
        extends JpaRepository<VoterAccount, Long> {

    // Check duplicate username
    boolean existsByUsernameIgnoreCase(
            String username
    );

    // Check duplicate generated voter ID
    boolean existsByVoterId(
            String voterId
    );

    // Check whether the voter is already registered
    boolean existsByNameIgnoreCaseAndDateOfBirth(
            String name,
            LocalDate dateOfBirth
    );

    // Find registered voter during login
    Optional<VoterAccount> findByUsernameIgnoreCase(
            String username
    );

    // Find account using voter ID
    Optional<VoterAccount> findByVoterId(
            String voterId
    );
}