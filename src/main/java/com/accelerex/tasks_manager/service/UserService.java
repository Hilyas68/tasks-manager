package com.accelerex.tasks_manager.service;


import com.accelerex.tasks_manager.dto.*;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    List<GetUserResponse> getUsers();

    GetUserResponse getUserById(Long userId);

    String signUpUser(UserSignUpDto signUpDto);
    SignUpResponse adminSignUp(UserDto dto);

    PrincipalDTO loginUser(UserLoginDto dto);

    String updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);

    CreateStaffResponse createStaff(StaffDto staffCreationDto) throws MessagingException;

    String activateAccount(ActivationDto activationDto, String email);
}
