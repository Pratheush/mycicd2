package com.learncicd.customermgmtjenkins.service;


import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.exception.CustomerNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerService {

    private final List<Customer> customerList = new ArrayList<>();

    @PostConstruct
    public void initData() {
        log.info("initData called");
        customerList.add(new Customer("John", "Doe", 30, "1234567890"));
        customerList.add(new Customer("Jane", "Smith", 25, "9876543210"));
    }

    public void resetData(){
        log.info("resetData called");
        customerList.clear();
    }

    public List<Customer> getAllCustomers() {
        log.info("getAllCustomers called");
        return customerList;
    }

    public void addCustomer(Customer customer) {
        log.info("addCustomer called");
        customerList.add(customer);
    }

    public Customer getCustomer(String firstName) {
        log.info("getCustomer called");
        return customerList.stream()
                .filter(customer -> customer.getFirstName().equalsIgnoreCase(firstName))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not FOund With Name : "+ firstName));
    }
}
