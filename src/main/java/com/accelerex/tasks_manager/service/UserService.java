package com.accelerex.tasks_manager.service;


import com.accelerex.tasks_manager.dto.*;
import com.accelerex.tasks_manager.model.auth.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto signUpUser(UserSignUpDto signUpDto);
    UserDto adminSignUp(UserDto dto);

    PrincipalDTO loginUser(UserLoginDto dto);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);

    User createStaff(StaffDto staffCreationDto);

    String activateAccount(ActivationDto activationDto, String email);
}
