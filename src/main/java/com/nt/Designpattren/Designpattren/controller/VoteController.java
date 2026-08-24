package com.nt.Designpattren.Designpattren.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nt.Designpattren.Designpattren.model.Voter;
import com.nt.Designpattren.Designpattren.model.VoterAccount;
import com.nt.Designpattren.Designpattren.repo.VoterRepository;
import com.nt.Designpattren.Designpattren.service.ElectionService;
import com.nt.Designpattren.Designpattren.service.VoterAccountService;
import com.nt.Designpattren.Designpattren.service.VoterService;

import jakarta.servlet.http.HttpSession;

@Controller
public class VoteController {

    private final VoterService voterService;
    private final VoterAccountService accountService;
    private final ElectionService electionService;
    private final VoterRepository voterRepository;

    public VoteController(
            VoterService voterService,
            VoterAccountService accountService,
            ElectionService electionService,
            VoterRepository voterRepository) {

        this.voterService = voterService;
        this.accountService = accountService;
        this.electionService = electionService;
        this.voterRepository = voterRepository;
    }

    // =====================================================
    // SHOW VOTING PAGE
    // =====================================================

    @GetMapping("/voter/vote")
    public String votingPage(
            @RequestParam(required = false)
            String electionType,
            HttpSession session,
            Model model) {

        VoterAccount account =
                getLoggedInAccount(session);

        if (account == null) {
            return "redirect:/voter/login";
        }

        Voter voter = new Voter();

        // Use registered voter information
        voter.setName(account.getName());

        voter.setAge(
                accountService.calculateAge(
                        account.getDateOfBirth()
                )
        );

        voter.setVoterId(
                account.getVoterId()
        );

        // Preselect election from dashboard
        if ("Assembly Election".equals(electionType) ||
                "Lok Sabha Election".equals(electionType)) {

            voter.setElectionType(electionType);
        }

        /*
         * If the voter already voted in one election,
         * use the same previously selected state.
         */
        Voter previousVote =
                voterRepository.findFirstByVoterId(
                        account.getVoterId()
                );

        if (previousVote != null) {
            voter.setLocation(
                    previousVote.getLocation()
            );
        }

        model.addAttribute(
                "voter",
                voter
        );

        addCommonData(
                model,
                account
        );

        return "index";
    }

    // =====================================================
    // SUBMIT VOTE
    // =====================================================

    @PostMapping("/vote")
    public String submitVote(
            @ModelAttribute("voter")
            Voter voter,
            HttpSession session,
            Model model) {

        VoterAccount account =
                getLoggedInAccount(session);

        if (account == null) {
            return "redirect:/voter/login";
        }

        /*
         * Name, age and voter ID must always come from
         * the logged-in registered account.
         */
        voter.setName(
                account.getName()
        );

        voter.setAge(
                accountService.calculateAge(
                        account.getDateOfBirth()
                )
        );

        voter.setVoterId(
                account.getVoterId()
        );

        // Validate state
        if (voter.getLocation() == null ||
                voter.getLocation().trim().isEmpty()) {

            return showError(
                    model,
                    voter,
                    account,
                    "❌ Please select your State / UT."
            );
        }

        voter.setLocation(
                voter.getLocation().trim()
        );

        // Validate election
        if (voter.getElectionType() == null ||
                voter.getElectionType()
                        .trim()
                        .isEmpty()) {

            return showError(
                    model,
                    voter,
                    account,
                    "❌ Please select an Election Type."
            );
        }

        voter.setElectionType(
                voter.getElectionType().trim()
        );

        // Validate party
        if (voter.getParty() == null ||
                voter.getParty().trim().isEmpty()) {

            return showError(
                    model,
                    voter,
                    account,
                    "❌ Please select a Party."
            );
        }

        voter.setParty(
                voter.getParty().trim()
        );

        // Admin must enable the election
        if (!electionService.isElectionActive(
                voter.getElectionType())) {

            return showError(
                    model,
                    voter,
                    account,
                    "⛔ " + voter.getElectionType()
                            + " is currently inactive."
            );
        }

        // Prevent voting twice in the same election
        if (voterService.hasVoted(
                account.getVoterId(),
                voter.getElectionType())) {

            return showError(
                    model,
                    voter,
                    account,
                    "❌ You have already voted in this election."
            );
        }

        boolean saved =
                voterService.saveVoter(voter);

        if (!saved) {

            return showError(
                    model,
                    voter,
                    account,
                    "❌ Vote could not be submitted. "
                    + "Check the selected state, election and party."
            );
        }

        /*
         * Email is sent by VoterService.
         * Do not send it again from this controller.
         */
        model.addAttribute(
                "voterId",
                account.getVoterId()
        );

        model.addAttribute(
                "name",
                account.getName()
        );

        model.addAttribute(
                "location",
                voter.getLocation()
        );

        model.addAttribute(
                "electionType",
                voter.getElectionType()
        );

        model.addAttribute(
                "party",
                voter.getParty()
        );

        return "success";
    }

    // =====================================================
    // SHOW VOTING ERROR
    // =====================================================

    private String showError(
            Model model,
            Voter voter,
            VoterAccount account,
            String message) {

        model.addAttribute(
                "msg",
                message
        );

        model.addAttribute(
                "voter",
                voter
        );

        addCommonData(
                model,
                account
        );

        return "index";
    }

    // =====================================================
    // COMMON PAGE DATA
    // =====================================================

    private void addCommonData(
            Model model,
            VoterAccount account) {

        model.addAttribute(
                "account",
                account
        );

        model.addAttribute(
                "locations",
                voterService.getLocations()
        );

        model.addAttribute(
                "assemblyActive",
                electionService.isElectionActive(
                        "Assembly Election"
                )
        );

        model.addAttribute(
                "lokSabhaActive",
                electionService.isElectionActive(
                        "Lok Sabha Election"
                )
        );
    }

    // =====================================================
    // GET LOGGED-IN VOTER
    // =====================================================

    private VoterAccount getLoggedInAccount(
            HttpSession session) {

        Object accountId =
                session.getAttribute(
                        VoterAuthController.SESSION_ACCOUNT_ID
                );

        if (!(accountId instanceof Long)) {
            return null;
        }

        return accountService.findById(
                (Long) accountId
        ).orElse(null);
    }
}