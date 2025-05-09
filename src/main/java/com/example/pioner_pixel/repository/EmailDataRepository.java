package com.example.pioner_pixel.repository;

import com.example.pioner_pixel.model.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    List<EmailData> findByUserId(Long userId);
    boolean existsByEmail(String email);

    Optional<EmailData> findByEmail(String email);
}
