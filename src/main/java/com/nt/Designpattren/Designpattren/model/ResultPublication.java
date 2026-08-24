package com.nt.Designpattren.Designpattren.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "result_publication")
public class ResultPublication {

    /*
     * Only one row is required.
     * Its ID will always be 1.
     */
    @Id
    private Long id;

    @Column(nullable = false)
    private boolean published;

    public ResultPublication() {
    }

    public ResultPublication(
            Long id,
            boolean published) {

        this.id = id;
        this.published = published;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}