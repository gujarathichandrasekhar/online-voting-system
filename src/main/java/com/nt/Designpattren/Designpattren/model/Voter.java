package com.nt.Designpattren.Designpattren.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Voter {

    // =====================================================
    // PRIMARY KEY
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // NAME
    // =====================================================

    @Column(nullable = false)
    private String name;


    // =====================================================
    // AGE
    // =====================================================

    @Column(nullable = false)
    private int age;


    // =====================================================
    // STATE / LOCATION
    // =====================================================

    @Column(nullable = false)
    private String location;


    // =====================================================
    // ELECTION TYPE
    //
    // Examples:
    // Assembly Election
    // Lok Sabha Election
    // =====================================================

    @Column(nullable = false)
    private String electionType;


    // =====================================================
    // PARTY
    // =====================================================

    @Column(nullable = false)
    private String party;


    // =====================================================
    // VOTER ID
    //
    // IMPORTANT:
    // Do NOT use unique = true here.
    //
    // The same voter can have the same Voter ID
    // for different election types.
    //
    // Example:
    //
    // Chandrika | VOTER-12345 | Assembly Election
    // Chandrika | VOTER-12345 | Lok Sabha Election
    //
    // =====================================================

    @Column(nullable = false)
    private String voterId;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public Voter() {
    }


    // =====================================================
    // GETTER / SETTER - ID
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =====================================================
    // GETTER / SETTER - NAME
    // =====================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // =====================================================
    // GETTER / SETTER - AGE
    // =====================================================

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    // =====================================================
    // GETTER / SETTER - LOCATION
    // =====================================================

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    // =====================================================
    // GETTER / SETTER - ELECTION TYPE
    // =====================================================

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }


    // =====================================================
    // GETTER / SETTER - PARTY
    // =====================================================

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }


    // =====================================================
    // GETTER / SETTER - VOTER ID
    // =====================================================

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }
}