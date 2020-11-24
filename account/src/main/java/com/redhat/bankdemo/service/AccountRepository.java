package com.redhat.bankdemo.service;

import java.util.List;

import com.redhat.bankdemo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public List<Account> readAll() {
      return this.jdbcTemplate.query("SELECT * FROM account", rowMapper);
    }

    public List<Account> findByCustomerId(String customerId) {
      return this.jdbcTemplate.query("SELECT a.* FROM account a" +
          " LEFT OUTER JOIN customer_account_xref c ON a.accountId = c.accountId" +
          " WHERE c.customerId = ?", new Object[]{customerId}, rowMapper);
    }
}
