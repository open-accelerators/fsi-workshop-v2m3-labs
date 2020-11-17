package com.redhat.bankdemo.service;

import java.util.List;

import com.redhat.bankdemo.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

  //TODO: Autowire the jdbcTemplate here
  @Autowired
  private JdbcTemplate jdbcTemplate;

  //TODO: Add row mapper here
  private RowMapper<Customer> rowMapper = (rs, rowNum) -> new Customer(
          rs.getLong("customerId") ,
          rs.getString("lastName"),
          rs.getString("firstName"),
          rs.getString("middleInitial"),
          rs.getString("street"),
          rs.getString("city"),
          rs.getString("state"),
          rs.getString("zip"),
          rs.getString("phone"),
          rs.getString("email")
  );

  //TODO: Create a method for returning all customers
  public List<Customer> readAll() {
    return jdbcTemplate.query("SELECT * FROM customer", rowMapper);
  }

  //TODO: Create a method for returning one customer
  public Customer findById(Long id) {
    return jdbcTemplate.queryForObject("SELECT * FROM catalog WHERE customerId = ?", new Object[] {id}, rowMapper);
  }

}
