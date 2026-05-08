package com.chirag.phase5.service.impl;

import com.chirag.phase5.dto.response.UserResponse;
import com.chirag.phase5.entity.LogEntry;
import com.chirag.phase5.entity.Role;
import com.chirag.phase5.entity.User;
import com.chirag.phase5.repository.LogRepository;
import com.chirag.phase5.repository.UserRepository;
import com.chirag.phase5.service.AdminService;
import com.chirag.phase5.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Override
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Users fetched successfully", userResponses);
    }

    @Override
    public ApiResponse<UserResponse> getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }
        return new ApiResponse<>(true, "User fetched successfully", mapToUserResponse(userOpt.get()));
    }

    @Override
    public ApiResponse<String> blockUnblockUser(Long id, boolean block) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        User user = userOpt.get();
        user.setIsActive(!block); // If block is true, isActive becomes false
        userRepository.save(user);

        String action = block ? "BLOCK_USER" : "UNBLOCK_USER";
        logAction(action, "SUCCESS", user);

        return new ApiResponse<>(true, "User " + (block ? "blocked" : "unblocked") + " successfully", null);
    }

    @Override
    public ApiResponse<String> deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }
        
        User user = userOpt.get();
        userRepository.deleteById(id);
        
        logAction("DELETE_USER", "SUCCESS", user);
        
        return new ApiResponse<>(true, "User deleted successfully", null);
    }

    @Override
    public ApiResponse<String> changeUserRole(Long id, Role role) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return new ApiResponse<>(false, "User not found", null);
        }

        User user = userOpt.get();
        user.setRole(role);
        userRepository.save(user);

        logAction("CHANGE_ROLE", "SUCCESS", user);

        return new ApiResponse<>(true, "User role changed successfully", null);
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
                .user(user) // The user the action was performed ON
                .build();
        logRepository.save(log);
    }
}
