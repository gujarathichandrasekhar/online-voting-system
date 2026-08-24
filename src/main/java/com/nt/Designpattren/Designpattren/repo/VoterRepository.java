package com.nt.Designpattren.Designpattren.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Designpattren.Designpattren.model.Voter;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

    // =====================================================
    // VOTER ID
    // =====================================================

    boolean existsByVoterId(String voterId);

    List<Voter> findByVoterId(String voterId);

    /*
     * Used by VoterService to find the first vote
     * made using this Voter ID.
     */
    Voter findFirstByVoterId(String voterId);


    // =====================================================
    // VOTER ID + ELECTION TYPE
    // =====================================================

    /*
     * Prevents the same Voter ID from voting twice
     * in the same election.
     *
     * Same Voter ID + Assembly Election -> NOT allowed
     * Same Voter ID + Lok Sabha Election -> allowed
     */
    boolean existsByVoterIdAndElectionType(
            String voterId,
            String electionType
    );


    // =====================================================
    // NAME
    // =====================================================

    boolean existsByNameIgnoreCase(String name);


    // =====================================================
    // NAME + ELECTION TYPE
    // =====================================================

    /*
     * Prevents the same person/name from voting twice
     * in the same election.
     */
    boolean existsByNameIgnoreCaseAndElectionType(
            String name,
            String electionType
    );


    // =====================================================
    // NAME + LOCATION
    // =====================================================

    boolean existsByNameIgnoreCaseAndLocation(
            String name,
            String location
    );


    // =====================================================
    // FIND VOTER BY NAME
    // =====================================================

    List<Voter> findByNameIgnoreCase(
            String name
    );


    // =====================================================
    // FIND VOTER BY NAME + ELECTION
    // =====================================================

    Voter findByNameIgnoreCaseAndElectionType(
            String name,
            String electionType
    );


    // =====================================================
    // PARTY
    // =====================================================

    long countByParty(
            String party
    );


    long countByPartyAndElectionType(
            String party,
            String electionType
    );


    // =====================================================
    // LOCATION + PARTY
    // =====================================================

    List<Voter> findByLocationAndParty(
            String location,
            String party
    );


    long countByLocationAndParty(
            String location,
            String party
    );


    // =====================================================
    // LOCATION + PARTY + ELECTION
    // =====================================================

    List<Voter> findByLocationAndPartyAndElectionType(
            String location,
            String party,
            String electionType
    );


    long countByLocationAndPartyAndElectionType(
            String location,
            String party,
            String electionType
    );


    // =====================================================
    // ELECTION TYPE
    // =====================================================

    List<Voter> findByElectionType(
            String electionType
    );


    long countByElectionType(
            String electionType
    );


    // =====================================================
    // LOCATION + ELECTION
    // =====================================================

    List<Voter> findByLocationAndElectionType(
            String location,
            String electionType
    );


    long countByLocationAndElectionType(
            String location,
            String electionType
    );
}