package com.wallet.digital_wallet.controller;

import com.wallet.digital_wallet.dto.request.CreateUserRequest;
import com.wallet.digital_wallet.dto.request.UpdateUserRequest;
import com.wallet.digital_wallet.dto.response.ApiResponse;
import com.wallet.digital_wallet.dto.response.PagedResponse;
import com.wallet.digital_wallet.dto.response.UserResponse;
import com.wallet.digital_wallet.entity.User;
import com.wallet.digital_wallet.mapper.UserMapper;
import com.wallet.digital_wallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wallet.digital_wallet.dto.request.LoginRequest;
import com.wallet.digital_wallet.dto.response.JwtResponse;
import com.wallet.digital_wallet.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * REST controller for user management.
 *
 * <p>Base path: {@code /api/v1/users}
 * <p>Responsible for user registration, retrieval, update, listing, search, and deactivation.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    @Operation(summary = "Register new user")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", userMapper.toResponse(user)));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("Success", userMapper.toResponse(user)));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("Success", userMapper.toResponse(user)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update user")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        User user = userService.updateUser(id, request);
        return  ResponseEntity.ok(ApiResponse.success("User updates successfully", userMapper.toResponse(user)));
    }

    @DeleteMapping
    @Operation(summary = "Deactivate user")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }

    @GetMapping
    @Operation(summary = "List all users (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users = userService.getAllUsers(pageable);
        PagedResponse<UserResponse> response = PagedResponse. fromPage(users.map(userMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search users")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchUsers(query, pageable);
        PagedResponse<UserResponse> response = PagedResponse.fromPage(users.map(userMapper::toResponse));
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate the user (throws exception on failure)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPin())
            );

            // Load user details (fix: use correct method name)
            User user = userService.getUserByUsername(request.getUsername());

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());

            // Return JWT response (adjust to match JwtResponse constructor)
            return ResponseEntity.ok(new JwtResponse(token, user.getUsername()));
        } catch (AuthenticationException e) {
            // Handle invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or PIN");
        } catch (Exception e) {
            // Handle other errors (e.g., user not found)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }
}