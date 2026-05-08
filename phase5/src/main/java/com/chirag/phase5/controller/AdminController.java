package com.chirag.phase5.controller;

import com.chirag.phase5.dto.response.UserResponse;
import com.chirag.phase5.entity.Role;
import com.chirag.phase5.service.AdminService;
import com.chirag.phase5.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        ApiResponse<UserResponse> response = adminService.getUserById(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<ApiResponse<String>> blockUser(@PathVariable Long id) {
        // Here we just toggle the block to true as per image. Or you can add a boolean param.
        ApiResponse<String> response = adminService.blockUnblockUser(id, true); 
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<ApiResponse<String>> unblockUser(@PathVariable Long id) {
        ApiResponse<String> response = adminService.blockUnblockUser(id, false); 
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        ApiResponse<String> response = adminService.deleteUser(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<String>> changeUserRole(@PathVariable Long id, @RequestParam Role role) {
        ApiResponse<String> response = adminService.changeUserRole(id, role);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }
}
