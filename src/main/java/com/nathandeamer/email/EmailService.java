package com.nathandeamer.email;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void doSomething(Order order) {
        System.out.println(order);
    }
}
