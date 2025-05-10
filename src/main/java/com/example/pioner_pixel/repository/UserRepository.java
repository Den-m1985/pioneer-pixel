package com.example.pioner_pixel.repository;


import com.example.pioner_pixel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT e.user.id FROM EmailData e WHERE e.email = :email")
    Optional<Long> findUserIdByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.emails " +
            "WHERE u.id = :userId")
    Optional<User> findUserWithDetailsById(@Param("userId") Long userId);
}
