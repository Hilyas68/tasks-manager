package com.accelerex.tasks_manager.service;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(String email, String subject, String body) throws MessagingException;

}
