package com.nt.Designpattren.Designpattren.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.Designpattren.Designpattren.model.VoterAccount;
import com.nt.Designpattren.Designpattren.repo.VoterAccountRepository;

@Service
public class VoterAccountService {

    private final VoterAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public VoterAccountService(
            VoterAccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // REGISTER NEW VOTER
    // =====================================================

    @Transactional
    public VoterAccount register(VoterAccount account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Registration details are required."
            );
        }

        // Voter name
        String name = account.getName() == null
                ? ""
                : account.getName().trim();

        if (name.length() < 3 || name.length() > 100) {
            throw new IllegalArgumentException(
                    "Name must contain between 3 and 100 characters."
            );
        }

        // Date of birth
        LocalDate dateOfBirth = account.getDateOfBirth();

        if (dateOfBirth == null) {
            throw new IllegalArgumentException(
                    "Please select your date of birth."
            );
        }

        int age = calculateAge(dateOfBirth);

        if (age < 18) {
            throw new IllegalArgumentException(
                    "You must be at least 18 years old to register."
            );
        }

        if (age > 120) {
            throw new IllegalArgumentException(
                    "Please enter a valid date of birth."
            );
        }

        // Check whether this voter is already registered
        if (accountRepository
                .existsByNameIgnoreCaseAndDateOfBirth(
                        name,
                        dateOfBirth
                )) {

            throw new IllegalArgumentException(
                    "This voter is already registered. Please login."
            );
        }

        // Username
        String username = account.getUsername() == null
                ? ""
                : account.getUsername()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        if (!username.matches("[a-z0-9._]{4,30}")) {
            throw new IllegalArgumentException(
                    "Username must contain 4–30 letters, numbers, dots or underscores."
            );
        }

        // Username cannot be the same as voter name
        String normalizedName = name
                .replaceAll("[\\s._]", "")
                .toLowerCase(Locale.ROOT);

        String normalizedUsername = username
                .replaceAll("[\\s._]", "")
                .toLowerCase(Locale.ROOT);

        if (normalizedName.equals(normalizedUsername)) {
            throw new IllegalArgumentException(
                    "Username cannot be the same as voter name."
            );
        }

        // Username must be unique
        if (accountRepository
                .existsByUsernameIgnoreCase(username)) {

            throw new IllegalArgumentException(
                    "This username is already registered."
            );
        }

        // Password
        String rawPassword = account.getPassword();

        if (rawPassword == null ||
                rawPassword.length() < 6 ||
                rawPassword.length() > 72) {

            throw new IllegalArgumentException(
                    "Password must contain between 6 and 72 characters."
            );
        }

        account.setName(name);
        account.setUsername(username);

        // Encrypt password
        account.setPassword(
                passwordEncoder.encode(rawPassword)
        );

        // Generate permanent voter ID
        account.setVoterId(
                generateUniqueVoterId()
        );

        return accountRepository.save(account);
    }

    // =====================================================
    // LOGIN VERIFICATION
    // =====================================================

    public Optional<VoterAccount> authenticate(
            String username,
            String rawPassword) {

        if (username == null ||
                username.trim().isEmpty() ||
                rawPassword == null ||
                rawPassword.isEmpty()) {

            return Optional.empty();
        }

        Optional<VoterAccount> account =
                accountRepository.findByUsernameIgnoreCase(
                        username.trim()
                );

        if (account.isPresent() &&
                passwordEncoder.matches(
                        rawPassword,
                        account.get().getPassword()
                )) {

            return account;
        }

        return Optional.empty();
    }

    // =====================================================
    // FIND ACCOUNT
    // =====================================================

    public Optional<VoterAccount> findById(Long accountId) {

        if (accountId == null) {
            return Optional.empty();
        }

        return accountRepository.findById(accountId);
    }

    // =====================================================
    // CALCULATE AGE
    // =====================================================

    public int calculateAge(LocalDate dateOfBirth) {

        if (dateOfBirth == null ||
                dateOfBirth.isAfter(LocalDate.now())) {

            return 0;
        }

        return Period.between(
                dateOfBirth,
                LocalDate.now()
        ).getYears();
    }

    // =====================================================
    // GENERATE UNIQUE VOTER ID
    // =====================================================

    private String generateUniqueVoterId() {

        String voterId;

        do {
            voterId = "VOTER-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();

        } while (
                accountRepository.existsByVoterId(voterId)
        );

        return voterId;
    }
}