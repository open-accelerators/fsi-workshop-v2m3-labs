package com.redhat.bankdemo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class TransactionResourceTest {

    @Test
    public void testCustomersEndpoint() {
        given()
          .when().get("/services/transactions")
          .then()
             .statusCode(200)
             .body(containsString("\"accountId\":\"5005\""));
    }

}