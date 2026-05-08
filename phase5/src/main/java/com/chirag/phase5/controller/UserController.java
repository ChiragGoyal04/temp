package com.chirag.phase5.controller;

import com.chirag.phase5.dto.request.LoginRequest;
import com.chirag.phase5.dto.request.RegisterRequest;
import com.chirag.phase5.dto.request.UpdateProfileRequest;
import com.chirag.phase5.dto.response.LoginResponse;
import com.chirag.phase5.dto.response.UserResponse;
import com.chirag.phase5.service.UserService;
import com.chirag.phase5.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterRequest request){
        ApiResponse<UserResponse> response = userService.registerUser(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest request){
        ApiResponse<LoginResponse> response = userService.loginUser(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(@RequestParam String email){
        ApiResponse<UserResponse> response = userService.getUserProfile(email);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @PutMapping("/user/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            @RequestParam String email, 
            @Valid @RequestBody UpdateProfileRequest request){
        ApiResponse<UserResponse> response = userService.updateUserProfile(email, request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @DeleteMapping("/user/profile")
    public ResponseEntity<ApiResponse<String>> deleteUserProfile(@RequestParam String email){
        ApiResponse<String> response = userService.deleteUserProfile(email);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }
}
