package com.redhat.bankdemo.client;

import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="transaction",fallbackFactory = TransactionClient.TransactionClientFallbackFactory.class)
public interface TransactionClient {

    @RequestMapping(method = RequestMethod.GET, value = "/services/transactions/{accountId}/balance", consumes = {MediaType.APPLICATION_JSON_VALUE})
    String checkBalance(@PathVariable("accountId") String accountId);

    @Component
    class TransactionClientFallbackFactory implements FallbackFactory<TransactionClient> {
      @Override
      public TransactionClient create(Throwable cause) {
        return accountId -> "{'balance':0, 'balanceDateTime':'January 1, 1970, 00:00:00 GMT'}";
      }
    }

}