package com.redhat.bankdemo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class TransactionHealthCheck implements HealthCheck {

  @Inject
  private TransactionResource transactionResource;

  public HealthCheckResponse call() {

    if (transactionResource.getAll() != null) {
      return HealthCheckResponse.named("Success of Transaction Health Check!!!").up().build();
    } else {
      return HealthCheckResponse.named("Failure of Transaction Health Check!!!").down().build();
    }
  }

}
