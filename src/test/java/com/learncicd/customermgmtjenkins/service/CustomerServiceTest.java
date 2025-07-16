package com.learncicd.customermgmtjenkins.service;


import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test for CustomerService
 */

class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setup() {
        customerService = new CustomerService();
        customerService.initData(); // initialize test data
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
    }

    @Test
    void testAddCustomer() {
        Customer newCustomer = new Customer("Bob", "Builder", 40, "1122334455");
        customerService.addCustomer(newCustomer);
        assertEquals(3, customerService.getAllCustomers().size());
    }

    @Test
    void testGetCustomerByFirstNameSuccess() {
        Customer customer = customerService.getCustomer("John");
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    void testGetCustomerByFirstNameNotFound() {

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomer("Ghost");
        });
        assertTrue(exception.getMessage().contains("Customer Not FOund With Name"));
    }

    /**
     *  a. Empty List Handling
     * Simulate the behavior when customerList is empty:
     */
    @Test
    void testGetAllCustomers_EmptyList() {
        CustomerService service = new CustomerService(); // do not call initData()
        assertTrue(service.getAllCustomers().isEmpty());
    }

    /**
     * Duplicate First Names
     * Since getCustomer(String firstName) returns the first match, you can test it like this:
     */
    @Test
    void testGetCustomer_DuplicateFirstNames() {
        CustomerService service = new CustomerService();
        service.initData();
        service.addCustomer(new Customer("John", "Smith", 45, "5555555555"));

        Customer result = service.getCustomer("John");

        assertEquals("Doe", result.getLastName()); // First John in the list
    }
}
