package com.accelerex.tasks_manager.dto;

import com.accelerex.tasks_manager.model.auth.enums.SecurityQuestion;
import lombok.Data;

@Data
public class ActivationDto {
    private String password;
    private String confirmPassword;
    private String otp;
    public SecurityQuestion securityQuestion;
    public String securityAnswer;
}
