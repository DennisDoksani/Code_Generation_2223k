package com.term4.BankingAppGrp1.cucumber.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TransactionStepDefinition extends BaseStepDefinition {

    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<String> response;
    private HttpHeaders httpHeaders;

    @Autowired
    private ObjectMapper mapper;

    private String token;

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {

        response = restTemplate.exchange(
                "/" + endpoint,
                HttpMethod.OPTIONS,
                new HttpEntity<>(
                        null,
                        httpHeaders),
                String.class);
        List<String> options = Arrays.stream(Objects.requireNonNull(response.getHeaders()
                        .get("Allow"))
                .get(0)
                .split(",")).toList();

        Assertions.assertTrue(options.contains(method.toUpperCase()));

    }


}
