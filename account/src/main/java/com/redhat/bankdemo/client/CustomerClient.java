package com.redhat.bankdemo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feign.hystrix.FallbackFactory;

@FeignClient(name="customer", fallbackFactory = CustomerClient.CustomerClientFallbackFactory.class)
public interface CustomerClient {

  @RequestMapping(method = RequestMethod.GET, value = "/services/customers/{customerId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
  String getCustomerStatus(@PathVariable("customerId") String customerId);

  //TODO: Add Fallback factory here
  @Component
  class CustomerClientFallbackFactory implements FallbackFactory<CustomerClient> {
    @Override
    public CustomerClient create(Throwable cause) {
      return customerId -> "[{'quantity': 1}]";
    }
  }
}
