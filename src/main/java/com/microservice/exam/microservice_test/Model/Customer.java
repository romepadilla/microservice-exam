package com.microservice.exam.microservice_test.Model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerNumber;

    @Column(nullable = false, length = 50)
    private String customerName;

    @Column(nullable = false, length = 20)
    private String customerMobile;

    @Column(nullable = false, unique = true, length = 50)
    @Email(message = "Invalid email format")
    private String customerEmail;

    @Column(nullable = false, length = 100)
    private String address1;

    @Column(length = 100)
    private String address2;

    @Column(nullable = false)
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @Override
    public String toString() {
        return "Customer{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                '}';
    }
}
