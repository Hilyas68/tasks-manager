package com.accelerex.tasks_manager.service;

public interface EmailService {
    void sendEmail(String email, String subject, String body);

}