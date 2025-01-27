package com.personalfinancetracker.domain.entities;

import com.personalfinancetracker.enums.AccountBankType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bank_accounts")
public class BankAccountEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity userEntity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountBankType type;

    @Column(nullable = false)
    private Double balance;

    private String name;
}
