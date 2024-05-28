package com.accelerex.tasks_manager.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PrincipalDTO {
    private String message;
    private boolean isSuccessful;
    private String token;


}
