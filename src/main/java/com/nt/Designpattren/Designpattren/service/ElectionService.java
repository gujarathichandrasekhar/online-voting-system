package com.nt.Designpattren.Designpattren.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Designpattren.Designpattren.model.Election;
import com.nt.Designpattren.Designpattren.repo.ElectionRepository;

@Service
public class ElectionService {

    @Autowired
    private ElectionRepository repository;


    // =====================================================
    // NORMALIZE ELECTION TYPE
    // =====================================================

    private String normalizeElectionType(
            String electionType) {

        if (electionType == null ||
                electionType.trim().isEmpty()) {

            return null;
        }

        String cleanType =
                electionType.trim();

        if (cleanType.equalsIgnoreCase("Assembly") ||
                cleanType.equalsIgnoreCase(
                        "Assembly Election")) {

            return "Assembly Election";
        }

        if (cleanType.equalsIgnoreCase("Lok Sabha") ||
                cleanType.equalsIgnoreCase(
                        "Lok Sabha Election")) {

            return "Lok Sabha Election";
        }

        return cleanType;
    }


    // =====================================================
    // FIND ELECTION
    // ALSO SUPPORTS OLD DATABASE NAMES
    // =====================================================

    private Optional<Election> findElection(
            String electionType) {

        String normalizedType =
                normalizeElectionType(electionType);

        if (normalizedType == null) {
            return Optional.empty();
        }

        // First search using the standard name
        Optional<Election> election =
                repository.findByElectionTypeIgnoreCase(
                        normalizedType
                );

        if (election.isPresent()) {
            return election;
        }

        // Search using the old name if necessary
        String originalType =
                electionType.trim();

        if (!normalizedType.equalsIgnoreCase(
                originalType)) {

            return repository
                    .findByElectionTypeIgnoreCase(
                            originalType
                    );
        }

        return Optional.empty();
    }


    // =====================================================
    // CHECK WHETHER ELECTION IS ACTIVE
    // =====================================================

    public boolean isElectionActive(
            String electionType) {

        Optional<Election> election =
                findElection(electionType);

        return election.isPresent()
                && election.get().getStatus() != null
                && "ACTIVE".equalsIgnoreCase(
                        election.get()
                                .getStatus()
                                .trim()
                );
    }


    // =====================================================
    // ENABLE ELECTION
    // =====================================================

    public void enableElection(
            String electionType) {

        String normalizedType =
                normalizeElectionType(electionType);

        if (normalizedType == null) {
            return;
        }

        Election election =
                findElection(electionType)
                        .orElseGet(Election::new);

        // Always save the standard election name
        election.setElectionType(
                normalizedType
        );

        election.setStatus("ACTIVE");

        repository.save(election);
    }


    // =====================================================
    // DISABLE ELECTION
    // =====================================================

    public void disableElection(
            String electionType) {

        String normalizedType =
                normalizeElectionType(electionType);

        if (normalizedType == null) {
            return;
        }

        Election election =
                findElection(electionType)
                        .orElseGet(Election::new);

        // Always save the standard election name
        election.setElectionType(
                normalizedType
        );

        election.setStatus("INACTIVE");

        repository.save(election);
    }


    // =====================================================
    // GET ELECTION STATUS
    // =====================================================

    public String getStatus(
            String electionType) {

        Optional<Election> election =
                findElection(electionType);

        if (election.isPresent()) {

            String status =
                    election.get().getStatus();

            if (status != null &&
                    !status.trim().isEmpty()) {

                return status
                        .trim()
                        .toUpperCase();
            }
        }

        return "INACTIVE";
    }
}