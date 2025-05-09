package com.example.pioner_pixel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "email_data")
public class EmailData extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Email
    @Size(max = 200)
    @Column(unique = true)
    private String email;
}

