package com.nt.Designpattren.Designpattren.model;

import jakarta.persistence.*;

@Entity
@Table(name = "elections")
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String electionType;

    @Column(nullable = false)
    private String status;

    public Election() {
    }

    public Election(String electionType, String status) {
        this.electionType = electionType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}