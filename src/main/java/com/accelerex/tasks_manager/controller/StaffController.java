package com.accelerex.tasks_manager.controller;

import com.accelerex.tasks_manager.dto.StaffDto;
import com.accelerex.tasks_manager.model.auth.User;
import com.accelerex.tasks_manager.model.auth.UserRole;
import com.accelerex.tasks_manager.repository.auth.UserRepository;
import com.accelerex.tasks_manager.repository.auth.UserRoleRepository;
import com.accelerex.tasks_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/staff")
public class StaffController {
    private final UserService userService;

    public StaffController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createStaff(@RequestBody StaffDto staffCreationDto) {
        User createdStaff = userService.createStaff(staffCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);
    }
}
