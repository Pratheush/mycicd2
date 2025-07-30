package com.learncicd.customermgmtjenkins.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
        log.info("CustomerNotFoundException called");
    }
}
