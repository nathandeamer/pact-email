package com.nathandeamer.email;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void doSomething(OrderEvent orderEvent) {
        System.out.println(orderEvent);
    }
}
