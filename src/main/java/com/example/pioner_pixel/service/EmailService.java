package com.example.pioner_pixel.service;

import com.example.pioner_pixel.model.EmailData;
import com.example.pioner_pixel.repository.EmailDataRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailDataRepository emailRepo;

    @Transactional
    public EmailData findEmail(String email) {
        return emailRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Email: " + email + " not found"));
    }

    public boolean ifEmailPresent(String email) {
        return emailRepo.existsByEmail(email);
    }

    public EmailData saveEmailData(EmailData email) {
        return emailRepo.save(email);
    }

    public void deleteEmail(EmailData emailData){
        emailRepo.delete(emailData);
    }
}
