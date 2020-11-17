package com.redhat.bankdemo.service;

import java.util.List;

import com.redhat.bankdemo.client.CustomerClient;
import com.redhat.bankdemo.model.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired
  private AccountRepository repository;

	//TODO: Autowire Account Client
	private CustomerClient customerClient;

	public Account read(String id) {
		Account account = repository.findById(id);
		//TODO: Update the customers in account by calling Customer service
		return account;
	}

	public List<Account> readAll() {
		List<Account> accountList = repository.readAll();
		//TODO: Update the customers in account by calling Customer service
		return accountList;
	}
  
}
