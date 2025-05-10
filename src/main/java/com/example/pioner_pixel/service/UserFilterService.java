package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.UserInfoDto;
import com.example.pioner_pixel.dto.UserSearchRequest;
import com.example.pioner_pixel.model.BaseEntity;
import com.example.pioner_pixel.model.EmailData;
import com.example.pioner_pixel.model.PhoneData;
import com.example.pioner_pixel.model.User;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFilterService {
    private final UserService userService;

    public Page<UserInfoDto> searchUsers(UserSearchRequest userSearchRequest) {
        Pageable pageable = PageRequest.of(userSearchRequest.page(), userSearchRequest.size());
        Specification<User> spec = Specification.where(null);
        if (userSearchRequest.name() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), userSearchRequest.name() + "%"));
        }
        if (userSearchRequest.email() != null) {
            spec = spec.and((root, query, cb) -> {
                Join<User, EmailData> emailJoin = root.join("emails");
                return cb.equal(emailJoin.get("email"), userSearchRequest.email());
            });
        }
        if (userSearchRequest.phone() != null) {
            spec = spec.and((root, query, cb) -> {
                Join<User, PhoneData> phoneJoin = root.join("phones");
                return cb.equal(phoneJoin.get("phone"), userSearchRequest.phone());
            });
        }
        if (userSearchRequest.dateOfBirth() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThan(root.get("dateOfBirth"), userSearchRequest.dateOfBirth()));
        }
        Page<User> usersPage = userService.findAll(spec, pageable);
        return usersPage.map(this::convertToUserInfoDto);
    }

    private UserInfoDto convertToUserInfoDto(User user) {
        List<Long> emailIds = user.getEmails().stream()
                .map(BaseEntity::getId)
                .toList();
        List<Long> phoneIds = user.getPhones().stream()
                .map(BaseEntity::getId)
                .toList();
        return new UserInfoDto(
                user.getName(),
                user.getDateOfBirth().toString(),
                user.getRole().toString(),
                user.getAccount().getId(),
                emailIds,
                phoneIds
        );
    }

}
