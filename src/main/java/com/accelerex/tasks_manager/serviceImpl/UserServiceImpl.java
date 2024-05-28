package com.accelerex.tasks_manager.serviceImpl;

import com.accelerex.tasks_manager.dto.*;
import com.accelerex.tasks_manager.exception.UserNotFoundException;
import com.accelerex.tasks_manager.model.auth.User;
import com.accelerex.tasks_manager.model.auth.UserRole;
import com.accelerex.tasks_manager.repository.auth.UserRepository;
import com.accelerex.tasks_manager.repository.auth.UserRoleRepository;
import com.accelerex.tasks_manager.security.JWTUtil;
import com.accelerex.tasks_manager.service.EmailService;
import com.accelerex.tasks_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final EmailService emailService;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users...");
        List<User> userList = userRepository.findAll();
        List<UserDto> responseList = new ArrayList<>();
        for (User user : userList) {
            UserDto response = new UserDto();
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
//            response.setRole(user.getRole());
            responseList.add(response);
        }
        log.info("Fetched {} users", responseList.size());
        return responseList;
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
//            userDto.setRole(user.getRole());
            return userDto;
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }


    @Override
    public UserDto signUpUser(UserSignUpDto signUpDto) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        newUser.setSecurityQuestion(signUpDto.getSecurityQuestion());
        newUser.setSecurityAnswer(signUpDto.getSecurityAnswer());
        User savedUser = userRepository.save(newUser);

        UserDto userDto = new UserDto();
        userDto.setId(savedUser.getId());
        userDto.setFirstName(savedUser.getFirstName());
        userDto.setLastName(savedUser.getLastName());
        userDto.setEmail(savedUser.getEmail());

        return userDto;
    }

    @Override
    public UserDto adminSignUp(UserDto dto) {
        Optional<User> optionalUser = userRepository.findUserByEmail(dto.getEmail());
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail(dto.getEmail());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setDisabled(true);
            Optional<UserRole> role = userRoleRepository.findById(dto.getRoleId());
            if (dto.getRoleId() != null && role.isPresent()) {
                user.setRole(role.get());
            }
            userRepository.save(user);
        } else {
            throw new RuntimeException("Email already exist");
        }
        return null;
    }

    @Override
    public PrincipalDTO loginUser(UserLoginDto dto) {
        PrincipalDTO principalDTO = new PrincipalDTO();
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {

            if (passwordEncoder.matches(dto.getPassword(), optionalUser.get().getPassword())) {
                principalDTO.setMessage("Successful");
                principalDTO.setSuccessful(true);
                principalDTO.setToken(jwtUtil.generateToken(dto.getEmail()));
            }
        } else {
            log.info("User not found");
            principalDTO.setMessage("Not Successful");
            principalDTO.setSuccessful(false);
        }

//        SecurityContextHolder.getContext().setAuthentication(authentication);


        return principalDTO;

    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            // Update the fields of the existing user with the values from userDto
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setEmail(userDto.getEmail());

            // Save the updated user to the repository
            User updatedUser = userRepository.save(existingUser);

            // Convert the updated user entity to a UserDto and return it
            UserDto updatedUserDto = new UserDto();
            updatedUserDto.setId(updatedUser.getId());
            updatedUserDto.setFirstName(updatedUser.getFirstName());
            updatedUserDto.setLastName(updatedUser.getLastName());
            updatedUserDto.setEmail(updatedUser.getEmail());

            return updatedUserDto;
        } else {
            // Handle the case where user with given userId is not found
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
        } else {
            // Handle the case where user with given userId is not found
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    @Override
    public User createStaff(StaffDto staffDto) {
        log.info("Creating new staff member with email: {}", staffDto.getEmail());

        Optional<User> optionalUser = userRepository.findUserByEmail(staffDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setFirstName(staffDto.getFirstName());
        newUser.setLastName(staffDto.getLastName());
        newUser.setEmail(staffDto.getEmail());
        Optional<UserRole> userRole = userRoleRepository.findById(2L);
        if (userRole.isEmpty()) {
            throw new RuntimeException("User role does not exist");
        }
        newUser.setRole(userRole.get());
        String otp = generateOTP();

        newUser.setOtp(otp);
        User savedUser = userRepository.save(newUser);


        // Generate activation link
        String activationLink = "http://localhost:8082/api/v1/users/activate?email=" + staffDto.getEmail();

        // Prepare email details
        String subject = "Welcome to Our Service!";
        String body = String.format(
                "Dear %s %s,\n\n" +
                        "Welcome to our service! We are excited to have you on board. Please activate your account using the link below:\n\n" +
                        "%s\n\n" +
                        "OTP: %s\n\n" +
                        "Best regards,\n" +
                        "The Team",
                newUser.getFirstName(),
                newUser.getLastName(),
                activationLink,
                otp
        );

        // Send the welcome email with activation link
        emailService.sendEmail(newUser.getEmail(), subject, body);

        log.info("New staff member created with ID: {}", savedUser.getId());
        return savedUser;
    }

    public String generateOTP() {
        Random random = new Random();
        // Generate a random number between 0 and 999999
        int otp = random.nextInt(1000000);

        // Convert the number to a string and pad with leading zeros if necessary
        String otpString = String.format("%06d", otp);

        return otpString;
    }

    @Override
    public String activateAccount(ActivationDto activationDto, String email) {
        if (!activationDto.getPassword().equals(activationDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        log.info("EMAIL {}", email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getOtp().equals(activationDto.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }
            user.setPassword(passwordEncoder.encode(activationDto.getPassword()));
            user.setDisabled(false); // Enable the user account
            user.setAccountVerified(true);
            user.setOtp(null);
            userRepository.save(user);

            return "Account activated successfully!";
        } else {
            throw new RuntimeException("User not found");
        }
    }
}





