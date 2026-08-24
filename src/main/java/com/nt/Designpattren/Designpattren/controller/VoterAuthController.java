package com.nt.Designpattren.Designpattren.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nt.Designpattren.Designpattren.model.VoterAccount;
import com.nt.Designpattren.Designpattren.service.ElectionService;
import com.nt.Designpattren.Designpattren.service.ResultPublicationService;
import com.nt.Designpattren.Designpattren.service.VoterAccountService;
import com.nt.Designpattren.Designpattren.service.VoterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class VoterAuthController {

    public static final String SESSION_ACCOUNT_ID =
            "loggedInVoterAccountId";

    private final VoterAccountService accountService;
    private final VoterService voterService;
    private final ElectionService electionService;
    private final ResultPublicationService resultPublicationService;

    public VoterAuthController(
            VoterAccountService accountService,
            VoterService voterService,
            ElectionService electionService,
            ResultPublicationService resultPublicationService) {

        this.accountService = accountService;
        this.voterService = voterService;
        this.electionService = electionService;
        this.resultPublicationService =
                resultPublicationService;
    }

    // =====================================================
    // LOGIN PAGE
    // =====================================================

    @GetMapping({"/", "/voter/login"})
    public String loginPage(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (getLoggedInAccount(session) != null) {
            return "redirect:/voter/dashboard";
        }

        return "voter-login";
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/voter/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            Model model) {

        Optional<VoterAccount> account =
                accountService.authenticate(
                        username,
                        password
                );

        if (account.isEmpty()) {

            model.addAttribute(
                    "msg",
                    "Invalid username or password. "
                    + "If you are not registered, "
                    + "click New Voter."
            );

            model.addAttribute(
                    "username",
                    username
            );

            return "voter-login";
        }

        /*
         * Remove any previous session before creating
         * the new voter login session.
         */
        HttpSession oldSession =
                request.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession newSession =
                request.getSession(true);

        newSession.setAttribute(
                SESSION_ACCOUNT_ID,
                account.get().getId()
        );

        // Automatically logout after 30 minutes
        newSession.setMaxInactiveInterval(
                30 * 60
        );

        return "redirect:/voter/dashboard";
    }

    // =====================================================
    // NEW VOTER REGISTRATION PAGE
    // =====================================================

    @GetMapping("/voter/register")
    public String registrationPage(
            HttpServletRequest request,
            Model model) {

        HttpSession session =
                request.getSession(false);

        if (getLoggedInAccount(session) != null) {
            return "redirect:/voter/dashboard";
        }

        model.addAttribute(
                "account",
                new VoterAccount()
        );

        addDateLimits(model);

        return "voter-register";
    }

    // =====================================================
    // REGISTER NEW VOTER
    // =====================================================

    @PostMapping("/voter/register")
    public String register(
            @ModelAttribute("account")
            VoterAccount account,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        String password =
                account.getPassword();

        if (password == null ||
                !password.equals(confirmPassword)) {

            account.setPassword(null);

            model.addAttribute(
                    "msg",
                    "Password and confirm password "
                    + "do not match."
            );

            addDateLimits(model);

            return "voter-register";
        }

        try {

            VoterAccount savedAccount =
                    accountService.register(account);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Registration successful. "
                    + "Login using your username "
                    + "and password."
            );

            redirectAttributes.addFlashAttribute(
                    "registeredVoterId",
                    savedAccount.getVoterId()
            );

            return "redirect:/voter/login";

        } catch (IllegalArgumentException e) {

            account.setPassword(null);

            model.addAttribute(
                    "msg",
                    e.getMessage()
            );

            addDateLimits(model);

            return "voter-register";
        }
    }

    // =====================================================
    // VOTER DASHBOARD
    // =====================================================

    @GetMapping("/voter/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        VoterAccount account =
                getLoggedInAccount(session);

        if (account == null) {
            return "redirect:/voter/login";
        }

        boolean assemblyActive =
                electionService.isElectionActive(
                        "Assembly Election"
                );

        boolean lokSabhaActive =
                electionService.isElectionActive(
                        "Lok Sabha Election"
                );

        boolean assemblyVoted =
                voterService.hasVoted(
                        account.getVoterId(),
                        "Assembly Election"
                );

        boolean lokSabhaVoted =
                voterService.hasVoted(
                        account.getVoterId(),
                        "Lok Sabha Election"
                );

        model.addAttribute(
                "account",
                account
        );

        model.addAttribute(
                "age",
                accountService.calculateAge(
                        account.getDateOfBirth()
                )
        );

        model.addAttribute(
                "assemblyActive",
                assemblyActive
        );

        model.addAttribute(
                "lokSabhaActive",
                lokSabhaActive
        );

        model.addAttribute(
                "assemblyVoted",
                assemblyVoted
        );

        model.addAttribute(
                "lokSabhaVoted",
                lokSabhaVoted
        );

        return "voter-dashboard";
    }

    // =====================================================
    // VOTER RESULTS
    // =====================================================

    @GetMapping("/voter/results")
    public String voterResults(
            HttpSession session) {

        VoterAccount account =
                getLoggedInAccount(session);

        if (account == null) {
            return "redirect:/voter/login";
        }

        /*
         * Until the administrator publishes results,
         * show the coming-soon page.
         */
        if (!resultPublicationService
                .areResultsPublished()) {

            return "result-coming-soon";
        }

        /*
         * After publication, show the actual report.
         */
        return "redirect:/report";
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @PostMapping("/voter/logout")
    public String logout(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/voter/login";
    }

    // =====================================================
    // GET LOGGED-IN VOTER
    // =====================================================

    private VoterAccount getLoggedInAccount(
            HttpSession session) {

        if (session == null) {
            return null;
        }

        Object accountId =
                session.getAttribute(
                        SESSION_ACCOUNT_ID
                );

        if (!(accountId instanceof Long)) {
            return null;
        }

        return accountService.findById(
                (Long) accountId
        ).orElse(null);
    }

    // =====================================================
    // REGISTRATION DOB LIMITS
    // =====================================================

    private void addDateLimits(Model model) {

        model.addAttribute(
                "maximumDob",
                LocalDate.now().minusYears(18)
        );

        model.addAttribute(
                "minimumDob",
                LocalDate.now().minusYears(120)
        );
    }
}