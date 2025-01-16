package com.microservice.exam.microservice_test.Repository;

import com.microservice.exam.microservice_test.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerEmail(String customerEmail);
    Optional<Customer> findByCustomerMobile(String customerMobile);
}
