package com.microservice.exam.microservice_test.Controller;

import com.microservice.exam.microservice_test.DTO.AccountCreationRequest;
import com.microservice.exam.microservice_test.DTO.TransactionResponse;
import com.microservice.exam.microservice_test.Exception.CustomerNotFoundException;
import com.microservice.exam.microservice_test.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        return ResponseEntity.status(201).body(accountService.createAccount(request));
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<TransactionResponse> getCustomerDetails(@PathVariable Long customerNumber) {
        try {
            TransactionResponse transactionResponse = accountService.getCustomerDetails(customerNumber);
            return ResponseEntity.status(200).body(transactionResponse);
        } catch (CustomerNotFoundException ex) {
            TransactionResponse transactionResponse = new TransactionResponse(null, 401, "Customer not found");
            return ResponseEntity.status(401).body(transactionResponse);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<TransactionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        TransactionResponse transactionResponse = new TransactionResponse(null, 401, "Please enter a correct email format");
        return ResponseEntity.status(401).body(transactionResponse);
    }
}
