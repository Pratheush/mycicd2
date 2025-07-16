package com.learncicd.customermgmtjenkins.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Customer {
    private String firstName;
    private String lastName;
    private int age;
    private String contactNumber;
}
