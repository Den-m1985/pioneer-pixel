package com.example.pioner_pixel.service;

import com.example.pioner_pixel.exceptions.MinimumRequiredException;
import com.example.pioner_pixel.model.AuthUser;
import com.example.pioner_pixel.model.EmailData;
import com.example.pioner_pixel.model.PhoneData;
import com.example.pioner_pixel.model.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {
    private final UserService userService;
    private final EmailService emailService;
    private final PhoneService phoneService;

    @Transactional
    public void addEmail(AuthUser authUser, String emailValue) {
        if (emailService.ifEmailPresent(emailValue)) {
            throw new EntityExistsException("Email " + emailValue + " already in use");
        }
        User user = authUser.getUser();
        EmailData emailToAdd = new EmailData();
        emailToAdd.setEmail(emailValue);
        emailToAdd.setUser(user);
        List<EmailData> emails = user.getEmails();
        emails.add(emailToAdd);
        user.setEmails(emails);
        userService.saveUser(user);
    }

    @Transactional
    public void addPhone(AuthUser authUser, String phoneValue) {
        if (phoneService.ifPhonePresent(phoneValue)) {
            throw new EntityExistsException("Phone " + phoneValue + " already in use");
        }
        User user = authUser.getUser();
        PhoneData phone = new PhoneData();
        phone.setPhone(phoneValue);
        phone.setUser(user);
        user.getPhones().add(phone);
        userService.saveUser(user);
    }

    @Transactional
    public void updateEmail(AuthUser authUser, String oldEmail, String newEmail) {
        if (emailService.ifEmailPresent(newEmail)) {
            throw new EntityExistsException("Email " + newEmail + " already in use");
        }
        User user = authUser.getUser();
        EmailData email = user.getEmails().stream()
                .filter(e -> e.getEmail().equals(oldEmail))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Old Email not found"));
        email.setEmail(newEmail);
        emailService.saveEmailData(email);
    }

    @Transactional
    public void updatePhone(AuthUser authUser, String oldPhone, String newPhone) {
        if (phoneService.ifPhonePresent(newPhone)) {
            throw new EntityExistsException("Phone " + newPhone + " already in use");
        }
        User user = authUser.getUser();
        PhoneData phone = user.getPhones().stream()
                .filter(p -> p.getPhone().equals(oldPhone))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Old phone not found"));
        phone.setPhone(newPhone);
        phoneService.savePhone(phone);
    }

    @Transactional
    public void deleteEmail(AuthUser authUser, String emailValue) {
        User user = authUser.getUser();
        if (user.getEmails().size() == 1) {
            throw new MinimumRequiredException("Forbidden to delete. Must be 1 email or more");
        }
        EmailData email = user.getEmails().stream()
                .filter(e -> e.getEmail().equals(emailValue))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));
        user.getEmails().remove(email);
        emailService.deleteEmail(email);
    }

    @Transactional
    public void deletePhone(AuthUser authUser, String phoneValue) {
        User user = authUser.getUser();
        if (user.getEmails().size() == 1) {
            throw new MinimumRequiredException("Forbidden to delete. Must be 1 phone or more");
        }
        PhoneData phone = user.getPhones().stream()
                .filter(p -> p.getPhone().equals(phoneValue))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Phone not found"));
        user.getPhones().remove(phone);
        phoneService.savePhone(phone);
    }
}
