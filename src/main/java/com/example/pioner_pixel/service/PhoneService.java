package com.example.pioner_pixel.service;

import com.example.pioner_pixel.model.PhoneData;
import com.example.pioner_pixel.repository.PhoneDataRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneDataRepository phoneRepo;

    public PhoneData findPhone(String phone) {
        return phoneRepo.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("phone: " + phone + " not found"));
    }

    public boolean ifPhonePresent(String phone) {
        return phoneRepo.existsByPhone(phone);
    }

    public void savePhone(PhoneData phoneData) {
        phoneRepo.save(phoneData);
    }

}
