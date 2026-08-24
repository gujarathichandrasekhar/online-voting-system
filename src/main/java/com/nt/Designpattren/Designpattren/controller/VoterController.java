package com.nt.Designpattren.Designpattren.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nt.Designpattren.Designpattren.model.Voter;
import com.nt.Designpattren.Designpattren.service.VoterService;

@Controller
public class VoterController {

    @Autowired
    private VoterService service;

    // =====================================================
    // STATE-WISE ELECTION REPORT
    // =====================================================

    @GetMapping("/report")
    public String report(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String electionType,
            Model model) {

        if (isBlank(state) || isBlank(electionType)) {

            addEmptyResultData(
                    model,
                    "Please select State and Election Type"
            );

            return "report";
        }

        state = state.trim();
        electionType = electionType.trim();

        Map<String, Long> results =
                getCleanResults(
                        state,
                        electionType
                );

        long total =
                calculateTotal(results);

        String winner =
                service.getWinner(
                        state,
                        electionType
                );

        if (isBlank(winner)) {
            winner = "No Winner Yet";
        }

        addResultData(
                model,
                state,
                electionType,
                results,
                winner,
                total
        );

        return "report";
    }

    // =====================================================
    // SEARCH PAGE
    // =====================================================

    @GetMapping("/search")
    public String searchPage(Model model) {

        model.addAttribute(
                "locations",
                service.getLocations()
        );

        return "search";
    }

    // =====================================================
    // GET PARTIES BY STATE
    // =====================================================

    @GetMapping("/parties")
    @ResponseBody
    public List<String> getParties(
            @RequestParam String location) {

        return service.getPartiesByLocation(
                location
        );
    }

    // =====================================================
    // GET PARTIES BY STATE + ELECTION
    // =====================================================

    @GetMapping("/parties-by-election")
    @ResponseBody
    public List<String> getPartiesByElection(
            @RequestParam String location,
            @RequestParam String electionType) {

        return service.getPartiesByLocationAndElection(
                location,
                electionType
        );
    }

    // =====================================================
    // SEARCH VOTER BY VOTER ID
    // =====================================================

    @PostMapping("/searchvoter")
    public String searchVoter(
            @RequestParam String voterId,
            Model model) {

        if (isBlank(voterId)) {

            model.addAttribute(
                    "msg",
                    "❌ Please enter Voter ID."
            );

            model.addAttribute(
                    "locations",
                    service.getLocations()
            );

            return "search";
        }

        voterId = voterId.trim();

        Voter voter =
                service.getVoterById(
                        voterId
                );

        if (voter == null) {

            model.addAttribute(
                    "msg",
                    "❌ No voter found with Voter ID: "
                            + voterId
            );

            model.addAttribute(
                    "locations",
                    service.getLocations()
            );

            return "search";
        }

        model.addAttribute(
                "voter",
                voter
        );

        return "voterdetails";
    }

    // =====================================================
    // VIEW ALL SUBMITTED VOTES
    // =====================================================

    @GetMapping("/voters")
    public String viewAllVoters(Model model) {

        model.addAttribute(
                "voters",
                service.getAllVoters()
        );

        return "voters";
    }

    // =====================================================
    // SEARCH BY LOCATION + PARTY
    // =====================================================

    @GetMapping("/search-by-location-party")
    public String searchByLocationAndParty(
            @RequestParam String location,
            @RequestParam String party,
            Model model) {

        if (isBlank(location) || isBlank(party)) {

            model.addAttribute(
                    "voters",
                    Collections.emptyList()
            );

            model.addAttribute(
                    "count",
                    0
            );

            return "location-party-results";
        }

        location = location.trim();
        party = party.trim();

        List<Voter> voters =
                service.searchVoters(
                        location,
                        party
                );

        model.addAttribute(
                "voters",
                voters
        );

        model.addAttribute(
                "count",
                voters.size()
        );

        model.addAttribute(
                "location",
                location
        );

        model.addAttribute(
                "party",
                party
        );

        return "location-party-results";
    }

    // =====================================================
    // SEARCH BY STATE + PARTY + ELECTION
    // =====================================================

    @GetMapping("/search-by-election")
    public String searchByElection(
            @RequestParam String location,
            @RequestParam String party,
            @RequestParam String electionType,
            Model model) {

        if (isBlank(location) ||
                isBlank(party) ||
                isBlank(electionType)) {

            model.addAttribute(
                    "voters",
                    Collections.emptyList()
            );

            model.addAttribute(
                    "count",
                    0
            );

            return "location-party-results";
        }

        location = location.trim();
        party = party.trim();
        electionType = electionType.trim();

        List<Voter> voters =
                service.searchVoters(
                        location,
                        party,
                        electionType
                );

        model.addAttribute(
                "voters",
                voters
        );

        model.addAttribute(
                "count",
                voters.size()
        );

        model.addAttribute(
                "location",
                location
        );

        model.addAttribute(
                "party",
                party
        );

        model.addAttribute(
                "electionType",
                electionType
        );

        return "location-party-results";
    }

    // =====================================================
    // WINNER PAGE
    // =====================================================

    @GetMapping("/winner")
    public String showWinner(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String electionType,
            Model model) {

        if (isBlank(state) || isBlank(electionType)) {

            addEmptyResultData(
                    model,
                    "Please select State and Election Type"
            );

            return "winner";
        }

        state = state.trim();
        electionType = electionType.trim();

        Map<String, Long> results =
                getCleanResults(
                        state,
                        electionType
                );

        long total =
                calculateTotal(results);

        String winner =
                service.getWinner(
                        state,
                        electionType
                );

        if (isBlank(winner)) {
            winner = "No Winner Yet";
        }

        addResultData(
                model,
                state,
                electionType,
                results,
                winner,
                total
        );

        return "winner";
    }

    // =====================================================
    // GET CLEAN PARTY RESULTS
    // =====================================================

    private Map<String, Long> getCleanResults(
            String state,
            String electionType) {

        Map<String, Long> serviceResults =
                service.getPartyResultsByState(
                        state,
                        electionType
                );

        Map<String, Long> results =
                new LinkedHashMap<>();

        if (serviceResults != null) {
            results.putAll(serviceResults);
        }

        results.entrySet().removeIf(
                entry ->
                        entry.getKey() == null ||
                        entry.getValue() == null
        );

        return results;
    }

    // =====================================================
    // CALCULATE TOTAL VOTES
    // =====================================================

    private long calculateTotal(
            Map<String, Long> results) {

        long total = 0L;

        for (Long votes : results.values()) {

            if (votes != null) {
                total += votes;
            }
        }

        return total;
    }

    // =====================================================
    // ADD RESULTS TO MODEL
    // =====================================================

    private void addResultData(
            Model model,
            String state,
            String electionType,
            Map<String, Long> results,
            String winner,
            long total) {

        model.addAttribute(
                "state",
                state
        );

        model.addAttribute(
                "electionType",
                electionType
        );

        model.addAttribute(
                "results",
                results
        );

        model.addAttribute(
                "winner",
                winner
        );

        model.addAttribute(
                "total",
                total
        );
    }

    // =====================================================
    // EMPTY RESULT DATA
    // =====================================================

    private void addEmptyResultData(
            Model model,
            String winnerMessage) {

        model.addAttribute(
                "state",
                ""
        );

        model.addAttribute(
                "electionType",
                ""
        );

        model.addAttribute(
                "results",
                Collections.emptyMap()
        );

        model.addAttribute(
                "winner",
                winnerMessage
        );

        model.addAttribute(
                "total",
                0L
        );
    }

    // =====================================================
    // NULL / EMPTY CHECK
    // =====================================================

    private boolean isBlank(String value) {

        return value == null ||
                value.trim().isEmpty();
    }
}