package com.nt.Designpattren.Designpattren.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nt.Designpattren.Designpattren.model.Voter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail;
    private final String adminEmail;

    public EmailService(
            JavaMailSender mailSender,

            @Value("${spring.mail.username}")
            String senderEmail,

            @Value("${app.admin.email}")
            String adminEmail) {

        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
        this.adminEmail = adminEmail;
    }

    public void sendVoteNotification(Voter voter) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            // Sender email from environment variable
            message.setFrom(senderEmail);

            // Admin email from environment variable
            message.setTo(adminEmail);

            message.setSubject(
                    "New Vote Submitted - "
                    + voter.getElectionType()
            );

            message.setText(

                    "Dear Admin,\n\n"

                    + "A new vote has been successfully submitted.\n\n"

                    + "==============================\n"
                    + "       NEW VOTE DETAILS\n"
                    + "==============================\n\n"

                    + "Voter Name    : "
                    + voter.getName()
                    + "\n"

                    + "Voter ID      : "
                    + voter.getVoterId()
                    + "\n"

                    + "Age           : "
                    + voter.getAge()
                    + "\n"

                    + "Location      : "
                    + voter.getLocation()
                    + "\n"

                    + "Election Type : "
                    + voter.getElectionType()
                    + "\n"

                    + "Party         : "
                    + voter.getParty()
                    + "\n\n"

                    + "==============================\n\n"

                    + "Online Voting System"
            );

            mailSender.send(message);

            System.out.println(
                    "Admin email notification sent successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "EMAIL SEND FAILED"
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );

            throw e;
        }
    }
}