package com.redhat.bankdemo.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.redhat.bankdemo.client.TransactionClient;
import com.redhat.bankdemo.model.Account;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private TransactionClient transactionClient;

    public List<Account> readAll() {
        List<Account> accountList = repository.readAll();
        accountList.forEach(account -> {
            updateBalanceOnTransactionClient(account);
        });
        return accountList;
    }

    public List<Account> findByCustomerRef(String customerId) {
        List<Account> accountList = repository.findByCustomerId(customerId);
        accountList.forEach(account -> {
            updateBalanceOnTransactionClient(account);
        });
        return accountList;
    }

    private void updateBalanceOnTransactionClient(Account account) {
        JSONArray jsonArray = new JSONArray(this.transactionClient.checkBalance(account.getAccountId()));
        List<String> balance = IntStream.range(0, jsonArray.length())
        .mapToObj(index -> ((JSONObject)jsonArray.get(index))
        .optString("balance")).collect(Collectors.toList());
        List<String> balanceDate = IntStream.range(0, jsonArray.length())
        .mapToObj(index -> ((JSONObject)jsonArray.get(index))
        .optString("balanceDateTime")).collect(Collectors.toList());
        Date dt = null;
        try {
        dt = DateFormat.getDateInstance(DateFormat.FULL).parse(balanceDate.get(0));
        if (dt.getTime() != 0) {
            account.setBalance(new BigDecimal(balance.get(0)));
        }
        } catch (ParseException e) { }
	}

}
