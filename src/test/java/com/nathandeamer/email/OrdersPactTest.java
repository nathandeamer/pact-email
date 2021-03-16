package com.nathandeamer.email;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@PactTestFor(providerName = "orders-topic", providerType = ProviderType.ASYNCH)
public class OrdersPactTest {

  @Autowired
  private OrderEventListener listener;

  @Pact(consumer = "email")
  MessagePact orderMessage(MessagePactBuilder builder) {
    PactDslJsonBody body = new PactDslJsonBody();
    body.integerType("id", 1234);
    body.stringMatcher("type", "^(CREATED|UPDATED|DELETED)$", "CREATED");

    Map<String, Object> metadata = new HashMap<>();
    metadata.put("Content-Type", "application/json");
    metadata.put("kafka_topic", "orders");

    return builder.expectsToReceive("a order created event")
            .withMetadata(metadata)
            .withContent(body)
            .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "orderMessage")
  void getOrderMessage(List<Message> messages) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    OrderEvent orderEvent = mapper.readValue(messages.get(0).contentsAsString(), OrderEvent.class);

    assertDoesNotThrow(() -> {
      listener.listen(orderEvent);
    });

    assertThat(orderEvent).isEqualTo(OrderEvent.builder().id(1234).type("CREATED").build());
  }

}