package com.nt.Designpattren.Designpattren.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Designpattren.Designpattren.model.ResultPublication;

@Repository
public interface ResultPublicationRepository
        extends JpaRepository<ResultPublication, Long> {
}