package com.chirag.phase5.service;

import com.chirag.phase5.dto.request.LoginRequest;
import com.chirag.phase5.dto.request.RegisterRequest;
import com.chirag.phase5.dto.request.UpdateProfileRequest;
import com.chirag.phase5.dto.response.LoginResponse;
import com.chirag.phase5.dto.response.UserResponse;
import com.chirag.phase5.util.ApiResponse;

public interface UserService {
    ApiResponse<UserResponse> registerUser(RegisterRequest request);
    ApiResponse<LoginResponse> loginUser(LoginRequest request);
    ApiResponse<UserResponse> getUserProfile(String email);
    ApiResponse<UserResponse> updateUserProfile(String email, UpdateProfileRequest request);
    ApiResponse<String> deleteUserProfile(String email);
}

