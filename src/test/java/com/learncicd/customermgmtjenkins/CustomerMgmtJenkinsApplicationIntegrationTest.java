package com.learncicd.customermgmtjenkins;

import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *  Use TestRestTemplate for Integration Tests
 * If you're using @SpringBootTest, prefer TestRestTemplate (provided by Spring Boot starter test)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class CustomerMgmtJenkinsApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
    void testGetAllCustomersReturnsDefaultTwo() {
        ResponseEntity<Customer[]> response = restTemplate.getForEntity("/api/customers/list-customers", Customer[].class);
        log.info("Customer List : {}", Arrays.stream(Objects.requireNonNull(response.getBody())).toList());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    void testGreetCustomerFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/customers/greet?firstName=John", String.class);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Good Morning, Welcome John Doe"));
        assertTrue(response.getBody().contains("Good Morning, Welcome John Doe"));
    }

    @Test
    void testCustomerNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/customers/greet?firstName=Ghost", String.class);
        assertEquals(404, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Customer Not Found With Name"));
        assertTrue(response.getBody().contains("Customer Not Found With Name"));
    }

    @Test
    void testAddCustomer() {
        Customer customer = new Customer("Elon", "Musk", 50, "1231231234");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/customers/save",customer, String.class);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Customer added successfully!"));
        assertTrue(response.getBody().contains("Customer added successfully!"));
    }
}
