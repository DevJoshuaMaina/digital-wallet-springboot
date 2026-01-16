package com.wallet.digital_wallet.repository;

import com.wallet.digital_wallet.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link User} persistence operations.
 *
 * <p>Documentation requirements:
 * <ul>
 *   <li>Uses Spring Data JPA derived queries</li>
 *   <li>Primary key type is {@link Long}</li>
 *   <li>Provides lookup by username/email and search support</li>
 * </ul>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by username.
     *
     * @param username username
     * @return optional user
     */
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    /**
     * Checks if a username already exists.
     *
     * @param username username to check
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email already exists.
     *
     * @param email email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:query% OR u.email LIKE %:query%")
    Page<User> searchUsers(@Param("query") String query, Pageable pageable);
}