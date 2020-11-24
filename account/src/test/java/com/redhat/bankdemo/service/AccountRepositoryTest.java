package com.redhat.bankdemo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.redhat.bankdemo.model.Account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class AccountRepositoryTest {

  //TODO: Insert Account Component here
  @Autowired
  AccountRepository repository;

  //TODO: Insert test_readAll here
  @Test
  public void test_readAll() {
    List<Account> accountList = repository.readAll();
    assertThat(accountList).isNotNull();
    assertThat(accountList).isNotEmpty();
    List<String> descriptions = accountList.stream().map(Account::getDescription).collect(Collectors.toList());
    assertThat(descriptions).contains("Hi Balance", "Checking", "Visa", "Super Interest Account");
  }

}
