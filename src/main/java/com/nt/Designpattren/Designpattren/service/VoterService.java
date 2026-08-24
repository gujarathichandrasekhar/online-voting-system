package com.nt.Designpattren.Designpattren.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Designpattren.Designpattren.model.Voter;
import com.nt.Designpattren.Designpattren.repo.VoterRepository;

@Service
public class VoterService {

    @Autowired
    private VoterRepository repo;

    // =========================================================
    // EMAIL SERVICE
    // =========================================================

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ElectionService electionService;

    // =========================================================
    // ELECTION TYPE -> STATE -> PARTIES
    // =========================================================

    private final Map<String, Map<String, List<String>>> electionParties =
            new LinkedHashMap<>();

    // =========================================================
    // PARTY -> SYMBOL IMAGE PATH
    // =========================================================

    private final Map<String, String> partySymbols =
            new LinkedHashMap<>();

    // =========================================================
    // STATES / UNION TERRITORIES
    // =========================================================

    private final List<String> locations = Arrays.asList(

            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
            "Delhi",
            "Jammu and Kashmir",
            "Ladakh",
            "Puducherry"
    );

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public VoterService() {

        // =====================================================
        // ASSEMBLY ELECTION
        // =====================================================

        Map<String, List<String>> assembly =
                new LinkedHashMap<>();

        assembly.put("Andhra Pradesh", Arrays.asList(
                "TDP",
                "YSR Congress Party",
                "Jana Sena Party",
                "BJP",
                "Indian National Congress",
                "CPI",
                "CPI(M)"
        ));

        assembly.put("Arunachal Pradesh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "National People's Party",
                "People's Party of Arunachal",
                "JD(U)"
        ));

        assembly.put("Assam", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "AGP",
                "AIUDF",
                "UPPL",
                "Bodoland People's Front",
                "CPI(M)",
                "CPI"
        ));

        assembly.put("Bihar", Arrays.asList(
                "BJP",
                "RJD",
                "JD(U)",
                "Indian National Congress",
                "LJP (Ram Vilas)",
                "HAM(S)",
                "CPI",
                "CPI(M)",
                "CPI(ML) Liberation"
        ));

        assembly.put("Chhattisgarh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP",
                "Janta Congress Chhattisgarh"
        ));

        assembly.put("Goa", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Aam Aadmi Party",
                "Goa Forward Party",
                "Maharashtrawadi Gomantak Party"
        ));

        assembly.put("Gujarat", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Aam Aadmi Party",
                "Bharatiya Tribal Party"
        ));

        assembly.put("Haryana", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Aam Aadmi Party",
                "INLD",
                "JJP",
                "BSP"
        ));

        assembly.put("Himachal Pradesh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "CPI(M)",
                "Aam Aadmi Party"
        ));

        assembly.put("Jharkhand", Arrays.asList(
                "BJP",
                "JMM",
                "Indian National Congress",
                "AJSU Party",
                "RJD",
                "CPI(ML) Liberation"
        ));

        assembly.put("Karnataka", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "JD(S)",
                "Aam Aadmi Party",
                "CPI(M)"
        ));

        assembly.put("Kerala", Arrays.asList(
                "Indian National Congress",
                "CPI(M)",
                "CPI",
                "BJP",
                "IUML",
                "Kerala Congress (M)",
                "RSP"
        ));

        assembly.put("Madhya Pradesh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP",
                "Aam Aadmi Party",
                "CPI",
                "CPI(M)"
        ));

        assembly.put("Maharashtra", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Shiv Sena",
                "Shiv Sena (UBT)",
                "NCP",
                "NCP (Sharadchandra Pawar)",
                "MNS",
                "VBA"
        ));

        assembly.put("Manipur", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "National People's Party",
                "Naga People's Front",
                "JD(U)"
        ));

        assembly.put("Meghalaya", Arrays.asList(
                "NPP",
                "Indian National Congress",
                "TMC",
                "UDP",
                "Voice of the People Party",
                "BJP"
        ));

        assembly.put("Mizoram", Arrays.asList(
                "MNF",
                "Zoram People's Movement",
                "Indian National Congress",
                "BJP"
        ));

        assembly.put("Nagaland", Arrays.asList(
                "NDPP",
                "BJP",
                "Naga People's Front",
                "Indian National Congress"
        ));

        assembly.put("Odisha", Arrays.asList(
                "BJP",
                "BJD",
                "Indian National Congress",
                "CPI(M)",
                "CPI"
        ));

        assembly.put("Punjab", Arrays.asList(
                "Aam Aadmi Party",
                "Indian National Congress",
                "BJP",
                "Shiromani Akali Dal",
                "BSP"
        ));

        assembly.put("Rajasthan", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP",
                "RLP",
                "CPI(M)"
        ));

        assembly.put("Sikkim", Arrays.asList(
                "SKM",
                "SDF",
                "Citizen Action Party-Sikkim",
                "BJP",
                "Indian National Congress"
        ));

        assembly.put("Tamil Nadu", Arrays.asList(
                "DMK",
                "AIADMK",
                "BJP",
                "Indian National Congress",
                "PMK",
                "NTK",
                "DMDK",
                "VCK",
                "CPI",
                "CPI(M)"
        ));

        assembly.put("Telangana", Arrays.asList(
                "Indian National Congress",
                "BRS",
                "BJP",
                "AIMIM",
                "CPI",
                "CPI(M)",
                "BSP"
        ));

        assembly.put("Tripura", Arrays.asList(
                "BJP",
                "CPI(M)",
                "Indian National Congress",
                "Tipra Motha Party"
        ));

        assembly.put("Uttar Pradesh", Arrays.asList(
                "BJP",
                "Samajwadi Party",
                "Indian National Congress",
                "BSP",
                "RLD",
                "Apna Dal (Soneylal)",
                "Suheldev Bharatiya Samaj Party"
        ));

        assembly.put("Uttarakhand", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP",
                "Aam Aadmi Party"
        ));

        assembly.put("West Bengal", Arrays.asList(
                "AITC",
                "BJP",
                "Indian National Congress",
                "CPI(M)",
                "CPI(ML) Liberation",
                "AIFB"
        ));

        assembly.put("Delhi", Arrays.asList(
                "BJP",
                "Aam Aadmi Party",
                "Indian National Congress",
                "BSP"
        ));

        assembly.put("Jammu and Kashmir", Arrays.asList(
                "JKNC",
                "PDP",
                "BJP",
                "Indian National Congress",
                "JKPC",
                "Apni Party",
                "CPI(M)"
        ));

        assembly.put("Ladakh", List.of());

        assembly.put("Puducherry", Arrays.asList(
                "AINRC",
                "Indian National Congress",
                "BJP",
                "DMK",
                "AIADMK",
                "PMK",
                "NTK"
        ));

        electionParties.put(
                "Assembly Election",
                assembly
        );

        // =====================================================
        // LOK SABHA ELECTION
        // =====================================================

        Map<String, List<String>> lokSabha =
                new LinkedHashMap<>();

        lokSabha.put("Andhra Pradesh", Arrays.asList(
                "TDP",
                "YSR Congress Party",
                "BJP",
                "Jana Sena Party",
                "Indian National Congress"
        ));

        lokSabha.put("Arunachal Pradesh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "National People's Party",
                "JD(U)"
        ));

        lokSabha.put("Assam", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "AGP",
                "UPPL",
                "AIUDF"
        ));

        lokSabha.put("Bihar", Arrays.asList(
                "BJP",
                "RJD",
                "JD(U)",
                "Indian National Congress",
                "LJP (Ram Vilas)",
                "CPI(ML) Liberation"
        ));

        lokSabha.put("Chhattisgarh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP"
        ));

        lokSabha.put("Goa", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Aam Aadmi Party"
        ));

        lokSabha.put("Gujarat", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Aam Aadmi Party"
        ));

        lokSabha.put("Haryana", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Aam Aadmi Party",
                "INLD"
        ));

        lokSabha.put("Himachal Pradesh", Arrays.asList(
                "BJP",
                "Indian National Congress"
        ));

        lokSabha.put("Jharkhand", Arrays.asList(
                "BJP",
                "JMM",
                "Indian National Congress",
                "AJSU Party"
        ));

        lokSabha.put("Karnataka", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "JD(S)"
        ));

        lokSabha.put("Kerala", Arrays.asList(
                "Indian National Congress",
                "CPI(M)",
                "CPI",
                "BJP",
                "IUML"
        ));

        lokSabha.put("Madhya Pradesh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP"
        ));

        lokSabha.put("Maharashtra", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Shiv Sena",
                "Shiv Sena (UBT)",
                "NCP",
                "NCP (Sharadchandra Pawar)"
        ));

        lokSabha.put("Manipur", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "National People's Party",
                "Naga People's Front"
        ));

        lokSabha.put("Meghalaya", Arrays.asList(
                "NPP",
                "Indian National Congress",
                "TMC",
                "BJP"
        ));

        lokSabha.put("Mizoram", Arrays.asList(
                "MNF",
                "Zoram People's Movement",
                "Indian National Congress",
                "BJP"
        ));

        lokSabha.put("Nagaland", Arrays.asList(
                "NDPP",
                "BJP",
                "Indian National Congress",
                "Naga People's Front"
        ));

        lokSabha.put("Odisha", Arrays.asList(
                "BJP",
                "BJD",
                "Indian National Congress"
        ));

        lokSabha.put("Punjab", Arrays.asList(
                "Aam Aadmi Party",
                "Indian National Congress",
                "BJP",
                "Shiromani Akali Dal"
        ));

        lokSabha.put("Rajasthan", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP",
                "RLP"
        ));

        lokSabha.put("Sikkim", Arrays.asList(
                "SKM",
                "SDF",
                "BJP",
                "Indian National Congress"
        ));

        lokSabha.put("Tamil Nadu", Arrays.asList(
                "DMK",
                "AIADMK",
                "BJP",
                "Indian National Congress",
                "PMK",
                "NTK",
                "VCK"
        ));

        lokSabha.put("Telangana", Arrays.asList(
                "Indian National Congress",
                "BJP",
                "BRS",
                "AIMIM"
        ));

        lokSabha.put("Tripura", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "CPI(M)",
                "Tipra Motha Party"
        ));

        lokSabha.put("Uttar Pradesh", Arrays.asList(
                "BJP",
                "Samajwadi Party",
                "Indian National Congress",
                "BSP",
                "RLD"
        ));

        lokSabha.put("Uttarakhand", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "BSP"
        ));

        lokSabha.put("West Bengal", Arrays.asList(
                "AITC",
                "BJP",
                "Indian National Congress",
                "CPI(M)"
        ));

        lokSabha.put("Delhi", Arrays.asList(
                "BJP",
                "Aam Aadmi Party",
                "Indian National Congress"
        ));

        lokSabha.put("Jammu and Kashmir", Arrays.asList(
                "JKNC",
                "PDP",
                "BJP",
                "Indian National Congress",
                "JKPC"
        ));

        lokSabha.put("Ladakh", Arrays.asList(
                "BJP",
                "Indian National Congress",
                "Independent"
        ));

        lokSabha.put("Puducherry", Arrays.asList(
                "AINRC",
                "Indian National Congress",
                "BJP",
                "DMK",
                "AIADMK"
        ));

        electionParties.put(
                "Lok Sabha Election",
                lokSabha
        );

        // =====================================================
        // INITIALIZE PARTY SYMBOLS
        // =====================================================

        initializePartySymbols();
    }

    // =========================================================
    // INITIALIZE PARTY SYMBOLS
    // =========================================================

    private void initializePartySymbols() {

        for (Map<String, List<String>> stateParties :
                electionParties.values()) {

            for (List<String> parties :
                    stateParties.values()) {

                for (String party : parties) {

                    if (party == null ||
                            party.trim().isEmpty()) {
                        continue;
                    }

                    String cleanParty = party.trim();

                    partySymbols.put(
                            cleanParty,
                            "/images/party-symbols/"
                                    + createSymbolFileName(cleanParty)
                                    + ".png"
                    );
                }
            }
        }

        partySymbols.put(
                "DEFAULT",
                "/images/party-symbols/default.png"
        );
    }

    // =========================================================
    // CREATE SYMBOL FILE NAME
    // =========================================================

    private String createSymbolFileName(String party) {

        if (party == null ||
                party.trim().isEmpty()) {
            return "default";
        }

        String fileName =
                party.trim()
                        .toLowerCase()
                        .replaceAll("[^a-z0-9]+", "-")
                        .replaceAll("^-+", "")
                        .replaceAll("-+$", "");

        if (fileName.isEmpty()) {
            return "default";
        }

        return fileName;
    }

    // =========================================================
    // GET PARTY SYMBOL
    // =========================================================

    public String getPartySymbol(String party) {

        if (party == null ||
                party.trim().isEmpty()) {
            return partySymbols.get("DEFAULT");
        }

        String cleanParty = party.trim();

        return partySymbols.getOrDefault(
                cleanParty,
                partySymbols.get("DEFAULT")
        );
    }

    // =========================================================
    // GET ALL PARTY SYMBOLS
    // =========================================================

    public Map<String, String> getPartySymbols() {

        return new LinkedHashMap<>(
                partySymbols
        );
    }

    // =========================================================
    // SAVE VOTER
    //
    // RULES:
    //
    // 1. Voter must be 18+
    // 2. Voter ID is required
    // 3. State must be valid
    // 4. Election type must be valid
    // 5. Party must belong to state + election
    // 6. Same Voter ID cannot vote twice in same election
    // 7. Same name cannot vote twice in same election
    // 8. Same voter can vote in different elections
    // 9. Voter's state is locked
    // 10. Email notification sent after successful save
    // =========================================================

    public boolean saveVoter(Voter voter) {

        if (voter == null) {
            return false;
        }

        // -----------------------------------------------------
        // AGE
        // -----------------------------------------------------

        if (voter.getAge() < 18) {
            return false;
        }

        // -----------------------------------------------------
        // NAME
        // -----------------------------------------------------

        if (voter.getName() == null ||
                voter.getName().trim().isEmpty()) {
            return false;
        }

        String name =
                voter.getName().trim();

        // -----------------------------------------------------
        // LOCATION
        // -----------------------------------------------------

        if (voter.getLocation() == null ||
                voter.getLocation().trim().isEmpty()) {
            return false;
        }

        String location =
                voter.getLocation().trim();

        if (!locations.contains(location)) {
            return false;
        }

        // -----------------------------------------------------
        // ELECTION TYPE
        // -----------------------------------------------------

        if (voter.getElectionType() == null ||
                voter.getElectionType().trim().isEmpty()) {
            return false;
        }

        String electionType =
                voter.getElectionType().trim();

        if (!electionParties.containsKey(electionType)) {
            return false;
        }

        // Admin must enable this election
        if (!electionService.isElectionActive(electionType)) {

            System.out.println(
                    electionType + " is currently disabled."
            );

            return false;
        }

        // -----------------------------------------------------
        // PARTY
        // -----------------------------------------------------

        if (voter.getParty() == null ||
                voter.getParty().trim().isEmpty()) {
            return false;
        }

        String party =
                voter.getParty().trim();

        // -----------------------------------------------------
        // GET PARTIES FOR ELECTION + STATE
        // -----------------------------------------------------

        Map<String, List<String>> stateParties =
                electionParties.get(electionType);

        List<String> parties =
                stateParties.get(location);

        if (parties == null ||
                parties.isEmpty()) {
            return false;
        }

        // -----------------------------------------------------
        // VERIFY PARTY
        // -----------------------------------------------------

        if (!parties.contains(party)) {
            return false;
        }

        // -----------------------------------------------------
        // VOTER ID
        // -----------------------------------------------------

        if (voter.getVoterId() == null ||
                voter.getVoterId().trim().isEmpty()) {
            return false;
        }

        String voterId =
                voter.getVoterId().trim();

        // -----------------------------------------------------
        // FIND PREVIOUS VOTE
        // -----------------------------------------------------

        Voter previousVote =
                repo.findFirstByVoterId(voterId);

        // -----------------------------------------------------
        // STATE LOCK
        // -----------------------------------------------------

        if (previousVote != null) {

            String previousLocation =
                    previousVote.getLocation();

            if (previousLocation != null &&
                    !previousLocation.trim()
                            .equalsIgnoreCase(location)) {

                return false;
            }
        }

        // -----------------------------------------------------
        // SAME VOTER ID + SAME ELECTION
        // -----------------------------------------------------

        boolean alreadyVoted =
                repo.existsByVoterIdAndElectionType(
                        voterId,
                        electionType
                );

        if (alreadyVoted) {
            return false;
        }

        // -----------------------------------------------------
        // SAME NAME + SAME ELECTION
        // -----------------------------------------------------


        // -----------------------------------------------------
        // CLEAN DATA
        // -----------------------------------------------------

        voter.setName(name);
        voter.setLocation(location);
        voter.setElectionType(electionType);
        voter.setParty(party);
        voter.setVoterId(voterId);

        // -----------------------------------------------------
        // SAVE VOTE
        // -----------------------------------------------------

        repo.save(voter);

        System.out.println(
                "Vote saved successfully for voter: "
                        + voter.getVoterId()
        );

        // -----------------------------------------------------
        // SEND EMAIL NOTIFICATION
        // -----------------------------------------------------

        try {

            emailService.sendVoteNotification(voter);

            System.out.println(
                    "Vote notification email sent successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Email notification failed: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        // -----------------------------------------------------
        // RETURN SUCCESS
        // -----------------------------------------------------

        return true;
    }

    // =========================================================
    // GET VOTES BY PARTY
    // =========================================================

    public long getVotes(String party) {

        return repo.countByParty(party);
    }

    // =========================================================
    // GET VOTES BY PARTY + ELECTION
    // =========================================================

    public long getVotes(
            String party,
            String electionType) {

        return repo.countByPartyAndElectionType(
                party,
                electionType
        );
    }

    // =========================================================
    // SEARCH VOTERS
    // LOCATION + PARTY
    // =========================================================

    public List<Voter> searchVoters(
            String location,
            String party) {

        return repo.findByLocationAndParty(
                location,
                party
        );
    }

    // =========================================================
    // SEARCH VOTERS
    // LOCATION + PARTY + ELECTION
    // =========================================================

    public List<Voter> searchVoters(
            String location,
            String party,
            String electionType) {

        return repo.findByLocationAndPartyAndElectionType(
                location,
                party,
                electionType
        );
    }

    // =========================================================
    // COUNT LOCATION + PARTY
    // =========================================================

    public long getVotesByLocationAndParty(
            String location,
            String party) {

        return repo.countByLocationAndParty(
                location,
                party
        );
    }

    // =========================================================
    // COUNT LOCATION + PARTY + ELECTION
    // =========================================================

    public long getVotesByLocationAndParty(
            String location,
            String party,
            String electionType) {

        return repo.countByLocationAndPartyAndElectionType(
                location,
                party,
                electionType
        );
    }

    // =========================================================
    // TOTAL VOTES
    // =========================================================

    public long totalVotes() {

        return repo.count();
    }

    // =========================================================
    // TOTAL VOTES BY ELECTION
    // =========================================================

    public long totalVotes(
            String electionType) {

        return repo.countByElectionType(
                electionType
        );
    }

    // =========================================================
    // TOTAL VOTES BY STATE + ELECTION
    // =========================================================

    public long totalVotes(
            String location,
            String electionType) {

        return repo.countByLocationAndElectionType(
                location,
                electionType
        );
    }

    // =========================================================
    // GET VOTER BY ID
    // =========================================================

    public Voter getVoterById(
            String voterId) {

        if (voterId == null ||
                voterId.trim().isEmpty()) {
            return null;
        }

        List<Voter> voters =
                repo.findByVoterId(
                        voterId.trim()
                );

        if (voters == null ||
                voters.isEmpty()) {
            return null;
        }

        return voters.get(0);
    }

    // =========================================================
    // GET ALL VOTERS
    // =========================================================

    public List<Voter> getAllVoters() {

        return repo.findAll();
    }

    // =========================================================
    // GET VOTERS BY ELECTION
    // =========================================================

    public List<Voter> getVotersByElectionType(
            String electionType) {

        if (electionType == null ||
                electionType.trim().isEmpty()) {
            return List.of();
        }

        return repo.findByElectionType(
                electionType.trim()
        );
    }

    // =========================================================
    // GET ALL PARTIES FOR ELECTION
    // =========================================================

    public List<String> getPartiesByElectionType(
            String electionType) {

        if (electionType == null ||
                electionType.trim().isEmpty()) {
            return List.of();
        }

        Map<String, List<String>> stateParties =
                electionParties.get(
                        electionType.trim()
                );

        if (stateParties == null) {
            return List.of();
        }

        List<String> allParties =
                new ArrayList<>();

        for (List<String> parties :
                stateParties.values()) {

            for (String party :
                    parties) {

                if (party != null &&
                        !party.trim().isEmpty() &&
                        !allParties.contains(
                                party.trim()
                        )) {

                    allParties.add(
                            party.trim()
                    );
                }
            }
        }

        return allParties;
    }

    // =========================================================
    // GET PARTIES BY LOCATION
    // DEFAULT = ASSEMBLY
    // =========================================================

    public List<String> getPartiesByLocation(
            String location) {

        return getPartiesByLocationAndElection(
                location,
                "Assembly Election"
        );
    }

    // =========================================================
    // GET PARTIES BY LOCATION + ELECTION
    // =========================================================

    public List<String> getPartiesByLocationAndElection(
            String location,
            String electionType) {

        if (location == null ||
                electionType == null) {
            return List.of();
        }

        location =
                location.trim();

        electionType =
                electionType.trim();

        // -----------------------------------------------------
        // CHECK LOCATION
        // -----------------------------------------------------

        if (!locations.contains(location)) {
            return List.of();
        }

        // -----------------------------------------------------
        // CHECK ELECTION
        // -----------------------------------------------------

        Map<String, List<String>> stateParties =
                electionParties.get(
                        electionType
                );

        if (stateParties == null) {
            return List.of();
        }

        // -----------------------------------------------------
        // RETURN PARTIES
        // -----------------------------------------------------

        return stateParties.getOrDefault(
                location,
                List.of()
        );
    }

    // =========================================================
    // GET ALL LOCATIONS
    // =========================================================

    public List<String> getLocations() {

        return List.copyOf(
                locations
        );
    }

    // =========================================================
    // WINNER - ALL ELECTIONS
    // =========================================================

    public String getWinner() {

        List<Voter> voters =
                repo.findAll();

        return calculateWinner(
                voters
        );
    }

    // =========================================================
    // WINNER BY ELECTION
    // =========================================================

    public String getWinner(
            String electionType) {

        if (electionType == null ||
                electionType.trim().isEmpty()) {
            return "No votes yet";
        }

        List<Voter> voters =
                repo.findByElectionType(
                        electionType.trim()
                );

        return calculateWinner(
                voters
        );
    }

    // =========================================================
    // WINNER BY STATE + ELECTION
    // =========================================================

    public String getWinner(
            String location,
            String electionType) {

        if (location == null ||
                electionType == null) {
            return "No votes yet";
        }

        location =
                location.trim();

        electionType =
                electionType.trim();

        List<Voter> voters =
                repo.findByLocationAndElectionType(
                        location,
                        electionType
                );

        return calculateWinner(
                voters
        );
    }

    // =========================================================
    // CALCULATE WINNER / TIE
    // =========================================================

    private String calculateWinner(
            List<Voter> voters) {

        if (voters == null ||
                voters.isEmpty()) {
            return "No votes yet";
        }

        Map<String, Long> voteCounts =
                new LinkedHashMap<>();

        // -----------------------------------------------------
        // COUNT VOTES
        // -----------------------------------------------------

        for (Voter voter :
                voters) {

            if (voter == null ||
                    voter.getParty() == null) {
                continue;
            }

            String party =
                    voter.getParty().trim();

            if (party.isEmpty()) {
                continue;
            }

            voteCounts.put(
                    party,
                    voteCounts.getOrDefault(
                            party,
                            0L
                    ) + 1
            );
        }

        if (voteCounts.isEmpty()) {
            return "No votes yet";
        }

        // -----------------------------------------------------
        // FIND MAXIMUM VOTES
        // -----------------------------------------------------

        long maxVotes = 0;

        for (Long votes :
                voteCounts.values()) {

            if (votes > maxVotes) {
                maxVotes = votes;
            }
        }

        // -----------------------------------------------------
        // FIND ALL PARTIES WITH MAXIMUM VOTES
        // -----------------------------------------------------

        List<String> winners =
                new ArrayList<>();

        for (Map.Entry<String, Long> entry :
                voteCounts.entrySet()) {

            if (entry.getValue() == maxVotes) {

                winners.add(
                        entry.getKey()
                );
            }
        }

        // -----------------------------------------------------
        // SINGLE WINNER
        // -----------------------------------------------------

        if (winners.size() == 1) {
            return winners.get(0);
        }

        // -----------------------------------------------------
        // TIE
        // -----------------------------------------------------

        return "TIE: " +
                String.join(
                        " & ",
                        winners
                );
    }

    // =========================================================
    // PARTY RESULTS BY ELECTION
    // =========================================================

    public Map<String, Long> getPartyResults(
            String electionType) {

        Map<String, Long> results =
                new LinkedHashMap<>();

        if (electionType == null ||
                electionType.trim().isEmpty()) {
            return results;
        }

        List<Voter> voters =
                repo.findByElectionType(
                        electionType.trim()
                );

        return calculatePartyResults(
                voters
        );
    }

    // =========================================================
    // PARTY RESULTS BY STATE + ELECTION
    // =========================================================

    public Map<String, Long> getPartyResultsByState(
            String location,
            String electionType) {

        Map<String, Long> results =
                new LinkedHashMap<>();

        if (location == null ||
                electionType == null ||
                location.trim().isEmpty() ||
                electionType.trim().isEmpty()) {

            return results;
        }

        location =
                location.trim();

        electionType =
                electionType.trim();

        List<Voter> voters =
                repo.findByLocationAndElectionType(
                        location,
                        electionType
                );

        return calculatePartyResults(
                voters
        );
    }

    // =========================================================
    // CALCULATE PARTY RESULTS
    // =========================================================

    private Map<String, Long> calculatePartyResults(
            List<Voter> voters) {

        Map<String, Long> results =
                new LinkedHashMap<>();

        if (voters == null ||
                voters.isEmpty()) {
            return results;
        }

        for (Voter voter :
                voters) {

            if (voter == null ||
                    voter.getParty() == null) {
                continue;
            }

            String party =
                    voter.getParty().trim();

            if (party.isEmpty()) {
                continue;
            }

            results.put(
                    party,
                    results.getOrDefault(
                            party,
                            0L
                    ) + 1
            );
        }

        return results;
    }

    // =========================================================
    // STATE + PARTY + ELECTION RESULT
    // =========================================================

    public long getVotesByStatePartyElection(
            String location,
            String party,
            String electionType) {

        return repo.countByLocationAndPartyAndElectionType(
                location,
                party,
                electionType
        );
    }
 // =========================================================
 // CHECK WHETHER VOTER ALREADY VOTED
 // =========================================================

 public boolean hasVoted(
         String voterId,
         String electionType) {

     if (voterId == null ||
             voterId.trim().isEmpty() ||
             electionType == null ||
             electionType.trim().isEmpty()) {

         return false;
     }

     return repo.existsByVoterIdAndElectionType(
             voterId.trim(),
             electionType.trim()
     );
 }
}
