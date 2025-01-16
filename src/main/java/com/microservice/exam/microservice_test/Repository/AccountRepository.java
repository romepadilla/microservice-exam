package com.microservice.exam.microservice_test.Repository;

import com.microservice.exam.microservice_test.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerCustomerNumber(Long customerNumber);
}
