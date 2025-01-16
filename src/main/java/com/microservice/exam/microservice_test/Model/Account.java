package com.microservice.exam.microservice_test.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Account {

    @Id
    @Column(nullable = false)
    private Long accountNumber;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private Double availableBalance;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountType='" + accountType + '\'' +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
