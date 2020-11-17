package com.redhat.bankdemo.service;

import java.util.List;

import com.redhat.bankdemo.model.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

  //TODO: Autowire the jdbcTemplate here
  @Autowired
  private JdbcTemplate jdbcTemplate;

  //TODO: Add row mapper here
  private RowMapper<Account> rowMapper = (rs, rowNum) -> new Account(
    rs.getString("accountId"),
    rs.getString("type"),
    rs.getString("description"),
    rs.getBigDecimal("balance"),
    rs.getDate("balanceDate"),
    rs.getBigDecimal("creditLine"),
    rs.getBigDecimal("beginBalance"),
    rs.getDate("beginBalanceTimestamp")
  );

  //TODO: Create a method for returning all accounts
  public List<Account> readAll() {
    return jdbcTemplate.query("SELECT * FROM account", rowMapper);
  }

  //TODO: Create a method for returning one customer
  public Account findById(String id) {
    return jdbcTemplate.queryForObject("SELECT * FROM account WHERE accountId = ?", new Object[] {id}, rowMapper);
  }
}
