package com.example.pioner_pixel.repository;

import com.example.pioner_pixel.model.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneDataRepository  extends JpaRepository<PhoneData, Long> {
    List<PhoneData> findByUserId(Long userId);
    boolean existsByPhone(String phone);

    Optional<PhoneData> findByPhone(String phone);
}
