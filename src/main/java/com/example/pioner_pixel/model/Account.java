package com.example.pioner_pixel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    public Account() {
        this.balance = BigDecimal.ZERO;
        this.initialDeposit = BigDecimal.ZERO;
    }

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @NotNull
    @DecimalMin(value = "0.00", message = "Balance must be >= 0")
    private BigDecimal balance;

    @NotNull
    @Column(name = "initial_deposit")
    private BigDecimal initialDeposit;
}
