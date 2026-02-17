package com.wallet.digital_wallet.service;

import com.wallet.digital_wallet.dto.request.CreateUserRequest;
import com.wallet.digital_wallet.dto.request.UpdateUserRequest;
import com.wallet.digital_wallet.entity.User;
import com.wallet.digital_wallet.entity.Wallet;
import com.wallet.digital_wallet.enums.UserStatus;
import com.wallet.digital_wallet.exception.DuplicateResourceException;
import com.wallet.digital_wallet.exception.ResourceNotFoundException;
import com.wallet.digital_wallet.repository.UserRepository;
import com.wallet.digital_wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for user lifecycle operations.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Enforces uniqueness of username and email</li>
 *   <li>Hashes PIN using BCrypt (never store raw PIN)</li>
 *   <li>Creates a Wallet automatically during registration</li>
 *   <li>Uses soft-deactivation via {@link UserStatus}</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor @Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user and auto-creates a wallet.
     *
     * <p>Transactional because it creates multiple records (User + Wallet) that must be consistent.
     *
     * @param request create user request DTO
     * @return created user entity (with wallet)
     * @throws DuplicateResourceException if username or email already exists
     */
    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .pinHash(passwordEncoder.encode(request.getPin()))
                .status(UserStatus.ACTIVE)
                .build();

        user =  userRepository.save(user);

        // Create wallet and attach bi-directionally (User.wallet mappedBy Wallet.user)
        Wallet wallet = Wallet.builder()
                .userId(user.getId())
                .walletNumber("WAL" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .balance(BigDecimal.ZERO)
                .dailyLimit(new BigDecimal("10000"))
                .build();

        walletRepository.save(wallet);
        log.info("User created with ID: {}", user.getId());
        return user;
    }


    /**
     * Returns a user by ID.
     *
     * @param id user ID
     * @return user entity
     * @throws ResourceNotFoundException if user does not exist
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    /**
     * Returns a user by username.
     *
     * @param username username
     * @return user entity
     * @throws ResourceNotFoundException if user does not exist
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Updates user profile fields.
     *
     * @param id user ID
     * @param request update DTO
     * @return updated user
     */
    @Transactional
    public User updateUser(Long id, UpdateUserRequest request) {
        User user = getUserById(id);
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        return userRepository.save(user);
    }

    /**
     * Soft-deactivates a user account.
     *
     * @param id user ID
     */
    @Transactional
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("User deactivated with ID: {}", id);
    }

    /**
     * Returns all users (paginated).
     *
     * @param pageable pagination settings
     * @return page of users
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Searches users by query across username and email (paginated).
     *
     * @param query search keyword
     * @param pageable pagination settings
     * @return page of matching users
     */
    public Page<User> searchUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String role = (user.getRole() != null) ? user.getRole() : "USER";
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPinHash(),
                authorities
        );
    }
}