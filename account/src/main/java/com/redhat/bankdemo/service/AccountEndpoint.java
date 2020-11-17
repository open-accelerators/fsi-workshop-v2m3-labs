package com.redhat.bankdemo.service;

import java.util.List;

import com.redhat.bankdemo.model.Account;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return accountService.readAll();
  }

  @GetMapping("/accounts/{accountId}")
  public Account read(@PathVariable("accountId") String id) {
    return accountService.read(id);
  }
}
