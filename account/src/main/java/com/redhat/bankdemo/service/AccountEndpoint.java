package com.redhat.bankdemo.service;

import java.util.List;
import com.redhat.bankdemo.model.Account;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/services")
public class AccountEndpoint {

    private final AccountService accountService;

    public AccountEndpoint(AccountService accountService) {
      this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<Account> readAll() {
      return this.accountService.readAll();
    }

    @GetMapping("/accounts/{customerId}")
    public List<Account> listAccounts(@PathVariable("customerId") String customerId) {
      return this.accountService.findByCustomerRef(customerId);
    }

}