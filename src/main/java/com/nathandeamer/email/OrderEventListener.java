package com.nathandeamer.email;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderEventListener {

	private final EmailService emailService;

	@KafkaListener(topics = "orders")
	public void listen(OrderEvent orderEvent) throws Exception {
		emailService.doSomething(orderEvent);
	}
}