package com.learncicd.customermgmtjenkins.service;


import com.learncicd.customermgmtjenkins.domain.Customer;
import com.learncicd.customermgmtjenkins.exception.CustomerNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final List<Customer> customerList = new ArrayList<>();

    @PostConstruct
    public void initData() {
        customerList.add(new Customer("John", "Doe", 30, "1234567890"));
        customerList.add(new Customer("Jane", "Smith", 25, "9876543210"));
    }

    public void resetData(){
        customerList.clear();
    }

    public List<Customer> getAllCustomers() {
        return customerList;
    }

    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }

    public Customer getCustomer(String firstName) {
        return customerList.stream()
                .filter(customer -> customer.getFirstName().equalsIgnoreCase(firstName))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not FOund With Name : "+ firstName));
    }
}
