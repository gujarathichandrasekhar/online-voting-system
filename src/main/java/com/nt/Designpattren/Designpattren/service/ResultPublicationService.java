package com.nt.Designpattren.Designpattren.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.Designpattren.Designpattren.model.ResultPublication;
import com.nt.Designpattren.Designpattren.repo.ResultPublicationRepository;

@Service
public class ResultPublicationService {

    private static final Long STATUS_ID = 1L;

    private final ResultPublicationRepository repository;

    public ResultPublicationService(
            ResultPublicationRepository repository) {

        this.repository = repository;
    }

    // =====================================================
    // CHECK WHETHER RESULTS ARE PUBLISHED
    // =====================================================

    @Transactional
    public boolean areResultsPublished() {

        return getOrCreateStatus()
                .isPublished();
    }

    // =====================================================
    // ADMIN PUBLISHES RESULTS
    // =====================================================

    @Transactional
    public void publishResults() {

        ResultPublication status =
                getOrCreateStatus();

        status.setPublished(true);

        repository.save(status);
    }

    // =====================================================
    // ADMIN HIDES RESULTS
    // =====================================================

    @Transactional
    public void hideResults() {

        ResultPublication status =
                getOrCreateStatus();

        status.setPublished(false);

        repository.save(status);
    }

    // =====================================================
    // CREATE DEFAULT STATUS
    //
    // Default: results are hidden
    // =====================================================

    private ResultPublication getOrCreateStatus() {

        return repository.findById(STATUS_ID)
                .orElseGet(() ->
                        repository.save(
                                new ResultPublication(
                                        STATUS_ID,
                                        false
                                )
                        )
                );
    }
}