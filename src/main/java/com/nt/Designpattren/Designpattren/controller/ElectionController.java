package com.nt.Designpattren.Designpattren.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nt.Designpattren.Designpattren.service.ElectionService;

@Controller
public class ElectionController {

    @Autowired
    private ElectionService electionService;


    // =====================================================
    // ADMIN ELECTION CONTROL PAGE
    // =====================================================

    @GetMapping("/admin/elections")
    public String electionControl(Model model) {

        model.addAttribute(
                "lokSabhaStatus",
                electionService.getStatus("Lok Sabha")
        );

        model.addAttribute(
                "assemblyStatus",
                electionService.getStatus("Assembly")
        );

        return "election-control";
    }


    // =====================================================
    // ENABLE ELECTION
    // =====================================================

    @PostMapping("/admin/election/enable")
    public String enableElection(
            @RequestParam String electionType) {

        electionService.enableElection(
                electionType
        );

        return "redirect:/admin/elections";
    }


    // =====================================================
    // DISABLE ELECTION
    // =====================================================

    @PostMapping("/admin/election/disable")
    public String disableElection(
            @RequestParam String electionType) {

        electionService.disableElection(
                electionType
        );

        return "redirect:/admin/elections";
    }
}