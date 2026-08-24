package com.nt.Designpattren.Designpattren.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.nt.Designpattren.Designpattren.controller.AdminController;
import com.nt.Designpattren.Designpattren.controller.VoterAuthController;
import com.nt.Designpattren.Designpattren.service.ResultPublicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class ResultAccessInterceptor
        implements HandlerInterceptor {

    private final ResultPublicationService
            resultPublicationService;

    public ResultAccessInterceptor(
            ResultPublicationService resultPublicationService) {

        this.resultPublicationService =
                resultPublicationService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session =
                request.getSession(false);

        /*
         * Logged-in administrators can always view
         * reports and winner information.
         */
        if (AdminController.isAdminLoggedIn(session)) {
            return true;
        }

        /*
         * A voter must be logged in.
         */
        if (session == null ||
                !(session.getAttribute(
                        VoterAuthController
                                .SESSION_ACCOUNT_ID
                ) instanceof Long)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/voter/login"
            );

            return false;
        }

        /*
         * A logged-in voter cannot view results
         * before the administrator publishes them.
         */
        if (!resultPublicationService
                .areResultsPublished()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/voter/results"
            );

            return false;
        }

        return true;
    }
}