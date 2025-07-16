package com.learncicd.customermgmtjenkins.integration;


import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class CustomerIntegrationTest {
    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private CustomerService customerService ;

    @BeforeEach
    void setup() {
        customerService.resetData();         // Clear data first
        customerService.initData();          // Then add default data
        log.info("setup() CUSTOMER INFO :: {}", customerService.getAllCustomers());
    }

    @AfterEach
    void reset() {
        customerService.resetData();
        log.info("reset() CUSTOMER INFO :: {}", customerService.getAllCustomers());
    }

    @Test
    void testGreetingIntegration() {
        String url = "http://localhost:" + port + "/api/customers/greet?firstName=John";
        String response = restTemplate.getForObject(url, String.class);
        assert response != null;
        assertTrue(response.contains("Good Morning, Welcome John"));
    }

    @Test
    void testAddCustomerIntegration() {
        String url = "http://localhost:" + port + "/api/customers/save";
        Customer customer = new Customer("Elon", "Musk", 50, "1231231234");

        String response = restTemplate.postForObject(url, customer, String.class);
        assertEquals("Customer added successfully!", response);
    }

    @Test
    void testGetAllCustomersReturnsDefaultTwo() {
        String url = "http://localhost:" +port + "/api/customers/list-customers";
        Customer[] response = restTemplate.getForObject(url, Customer[].class);
        //assertEquals(2, Objects.requireNonNull(response.length)); // remove redundant null check
        assert response != null;
        assertEquals(2, response.length);
    }
}
