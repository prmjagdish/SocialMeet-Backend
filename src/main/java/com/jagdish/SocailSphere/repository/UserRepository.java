package com.jagdish.SocailSphere.repository;

import com.jagdish.SocailSphere.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    @Query("""
        SELECT u FROM User u
        WHERE u.id <> :userId
        AND u.id NOT IN (
            SELECT f.id FROM User usr
            JOIN usr.following f
            WHERE usr.id = :userId
        )
    """)
    List<User> findSuggestedUsers(Long userId);
}
