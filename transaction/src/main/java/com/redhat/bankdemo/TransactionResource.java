package com.redhat.bankdemo;

import java.util.List;

import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/services/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

  @GET
  public List<Transaction> getAll() {
    return Transaction.listAll();
  }

  @GET
  @Path("{accountId}")
  public List<Transaction> findByAccountId(@PathParam String accountId) {
    return Transaction.listByAccountId(accountId);
  }

  @GET
  @Path("/{accountId}/statements/{year}-{month}")
  public List<Transaction> getStatement(@PathParam String accountId, @PathParam int year, @PathParam int month) {
    return Transaction.listByAccountAndPeriod(accountId, year, month);
  }

  @GET
  @Path("/{accountId}/balance")
  public Balance getBalance(@PathParam String accountId) {
    Transaction tx = Transaction.lastTransactionByAccountId(accountId);
    return Balance.fromTransaction(tx);
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
      int code = 500;
      if (exception instanceof WebApplicationException) {

      }
      return Response.status(code)
        .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
        .build();
    }
    
  }

}
