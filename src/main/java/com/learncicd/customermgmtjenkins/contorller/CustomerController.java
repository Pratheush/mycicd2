package com.learncicd.customermgmtjenkins.contorller;


import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET http://localhost:8080/api/customers/greet?firstName=tom
 * POST : curl -X POST http://localhost:8080/api/customers/save -H "Content-Type: application/json" -d '{"firstName": "Donald","lastName": "Trump","age": 53,"contactNumber": "8878787878787"}'
 * GET http://localhost:8080/api/customers/list-customers
 *
 */

@RestController
@Slf4j
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/list-customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // 🔹 Greet customer by first name
    @GetMapping("/greet")
    public String greetCustomer(@RequestParam String firstName) {
        Customer customer = customerService.getCustomer(firstName);
        return "Good Morning, Welcome " + customer.getFirstName() +" "+ customer.getLastName();
    }

    @PostMapping("/save")
    public String addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return "Customer added successfully!";
    }
}
