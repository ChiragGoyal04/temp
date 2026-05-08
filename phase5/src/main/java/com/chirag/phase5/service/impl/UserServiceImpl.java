package com.chirag.phase5.service.impl;

import com.chirag.phase5.dto.request.LoginRequest;
import com.chirag.phase5.dto.request.RegisterRequest;
import com.chirag.phase5.dto.request.UpdateProfileRequest;
import com.chirag.phase5.dto.response.LoginResponse;
import com.chirag.phase5.dto.response.UserResponse;
import com.chirag.phase5.entity.LogEntry;
import com.chirag.phase5.entity.Role;
import com.chirag.phase5.entity.User;
import com.chirag.phase5.repository.LogRepository;
import com.chirag.phase5.repository.UserRepository;
import com.chirag.phase5.service.UserService;
import com.chirag.phase5.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<UserResponse> registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse<>(false, "Email already exists", null);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        logAction("REGISTER", "SUCCESS", savedUser);

        return new ApiResponse<>(true, "User registered successfully", mapToUserResponse(savedUser));
    }

    @Override
    public ApiResponse<LoginResponse> loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "Invalid credentials", null);
        }

        User user = userOpt.get();

        if (!user.getIsActive()) {
            logAction("LOGIN", "FAILED - BLOCKED", user);
            return new ApiResponse<>(false, "Account is blocked. Contact admin.", null);
        }

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logAction("LOGIN", "SUCCESS", user);
            
            LoginResponse response = LoginResponse.builder()
                    .token("dummy-token-for-now") // JWT will go here later
                    .user(mapToUserResponse(user))
                    .build();
            
            return new ApiResponse<>(true, "Login successful", response);
        }

        logAction("LOGIN", "FAILED - WRONG PASSWORD", user);
        return new ApiResponse<>(false, "Invalid credentials", null);
    }

    @Override
    public ApiResponse<UserResponse> getUserProfile(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }
        return new ApiResponse<>(true, "Profile fetched successfully", mapToUserResponse(userOpt.get()));
    }

    @Override
    public ApiResponse<UserResponse> updateUserProfile(String email, UpdateProfileRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        User user = userOpt.get();
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            user.setName(request.getName());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // Email update might require more complex validation, ignoring for now for simplicity

        User updatedUser = userRepository.save(user);
        logAction("UPDATE_PROFILE", "SUCCESS", updatedUser);

        return new ApiResponse<>(true, "Profile updated successfully", mapToUserResponse(updatedUser));
    }

    @Override
    public ApiResponse<String> deleteUserProfile(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        User user = userOpt.get();
        user.setIsActive(false); // Soft delete
        userRepository.save(user);
        
        logAction("DELETE_ACCOUNT", "SUCCESS", user);

        return new ApiResponse<>(true, "Account deleted successfully", null);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAT())
                .updatedAt(user.getUpdatedAT())
                .build();
    }

    private void logAction(String action, String status, User user) {
        LogEntry log = LogEntry.builder()
                .action(action)
                .status(status)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .build();
        logRepository.save(log);
    }
}
