package com.nt.Designpattren.Designpattren.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nt.Designpattren.Designpattren.service.ResultPublicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    public static final String ADMIN_SESSION_KEY =
            "adminLoggedIn";

    private final ResultPublicationService
            resultPublicationService;

    private final String adminUsername;
    private final String adminPassword;

    public AdminController(
            ResultPublicationService resultPublicationService,

            @Value("${app.admin.username}")
            String adminUsername,

            @Value("${app.admin.password}")
            String adminPassword) {

        this.resultPublicationService =
                resultPublicationService;

        this.adminUsername =
                adminUsername;

        this.adminPassword =
                adminPassword;
    }

    // =====================================================
    // ADMIN LOGIN PAGE
    // =====================================================

    @GetMapping("/admin")
    public String loginPage(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (isAdminLoggedIn(session)) {
            return "redirect:/admin/dashboard";
        }

        return "admin";
    }

    // =====================================================
    // ADMIN LOGIN
    // =====================================================

    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            Model model) {

        if (adminUsername.equals(username) &&
                adminPassword.equals(password)) {

            HttpSession oldSession =
                    request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession =
                    request.getSession(true);

            newSession.setAttribute(
                    ADMIN_SESSION_KEY,
                    true
            );

            // Automatically logout after 30 minutes
            newSession.setMaxInactiveInterval(
                    30 * 60
            );

            return "redirect:/admin/dashboard";
        }

        model.addAttribute(
                "msg",
                "Invalid username or password"
        );

        return "admin";
    }

    // =====================================================
    // ADMIN DASHBOARD
    // =====================================================

    @GetMapping("/admin/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin";
        }

        model.addAttribute(
                "resultsPublished",
                resultPublicationService
                        .areResultsPublished()
        );

        return "admindashboard";
    }

    // =====================================================
    // PUBLISH RESULTS
    // =====================================================

    @PostMapping("/admin/results/publish")
    public String publishResults(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin";
        }

        resultPublicationService.publishResults();

        redirectAttributes.addFlashAttribute(
                "successMsg",
                "Election results published successfully."
        );

        return "redirect:/admin/dashboard";
    }

    // =====================================================
    // HIDE RESULTS
    // =====================================================

    @PostMapping("/admin/results/hide")
    public String hideResults(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin";
        }

        resultPublicationService.hideResults();

        redirectAttributes.addFlashAttribute(
                "successMsg",
                "Election results are now hidden from voters."
        );

        return "redirect:/admin/dashboard";
    }

    // =====================================================
    // ADMIN LOGOUT
    // =====================================================

    @PostMapping("/admin/logout")
    public String adminLogout(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/admin";
    }

    // =====================================================
    // CHECK ADMIN SESSION
    // =====================================================

    public static boolean isAdminLoggedIn(
            HttpSession session) {

        if (session == null) {
            return false;
        }

        Object loginStatus =
                session.getAttribute(
                        ADMIN_SESSION_KEY
                );

        return Boolean.TRUE.equals(loginStatus);
    }
}