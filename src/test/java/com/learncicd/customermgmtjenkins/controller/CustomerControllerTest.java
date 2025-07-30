package com.learncicd.customermgmtjenkins.controller;

import com.learncicd.customermgmtjenkins.contorller.CustomerController;
import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.exception.CustomerNotFoundException;
import com.learncicd.customermgmtjenkins.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit Test for CustomerController
 * Use @WebMvcTest to isolate controller logic, mocking the service layer
 */

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // use it when you are doing inline mocking
    @MockitoBean(answers = Answers.RETURNS_MOCKS)
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() throws Exception {
        Customer c1 = new Customer("John", "Doe", 30, "1234567890");
        Customer c2 = new Customer("Jane", "Smith", 25, "9876543210");

        Mockito.when(customerService.getAllCustomers()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/customers/list-customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testGreetCustomer() throws Exception {
        Customer customer = new Customer("Alice", "Wonder", 28, "1112223333");

        Mockito.when(customerService.getCustomer("Alice")).thenReturn(customer);

        mockMvc.perform(get("/api/customers/greet?firstName=Alice"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Good Morning, Welcome Alice Wonder")));
    }

    @Test
    void testAddCustomer() throws Exception {
        String json = """
                {
                    "firstName": "Bob",
                    "lastName": "Marley",
                    "age": 36,
                    "contactNumber": "9988776655"
                }
                """;

        mockMvc.perform(post("/api/customers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer added successfully!"));
    }

    /**
     * Add Unit Test for 404 in CustomerControllerTest
     */
    @Test
    void testGreetCustomer_NotFound() throws Exception {
        Mockito.when(customerService.getCustomer("Ghost"))
                .thenThrow(new CustomerNotFoundException("Customer Not FOund With Name : Ghost"));

        mockMvc.perform(get("/api/customers/greet?firstName=Ghost"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer Not FOund With Name : Ghost"));
    }
}
