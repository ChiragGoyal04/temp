package com.chirag.phase5.service;

import com.chirag.phase5.dto.response.UserResponse;
import com.chirag.phase5.entity.Role;
import com.chirag.phase5.util.ApiResponse;

import java.util.List;

public interface AdminService {
    ApiResponse<List<UserResponse>> getAllUsers();
    ApiResponse<UserResponse> getUserById(Long id);
    ApiResponse<String> blockUnblockUser(Long id, boolean block);
    ApiResponse<String> deleteUser(Long id);
    ApiResponse<String> changeUserRole(Long id, Role role);
}
