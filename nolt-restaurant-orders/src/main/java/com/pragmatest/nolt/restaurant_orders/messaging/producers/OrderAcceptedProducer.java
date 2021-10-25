package com.pragmatest.nolt.restaurant_orders.messaging.producers;

import com.pragmatest.nolt.restaurant_orders.messaging.events.OrderAcceptedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class OrderAcceptedProducer {

    @Value(value = "${order.accepted.topic}")
    private String orderAcceptedTopicName;

    @Autowired
    KafkaTemplate<String, OrderAcceptedEvent> orderAcceptedKafkaTemplate;

    public void send(OrderAcceptedEvent event) {
        orderAcceptedKafkaTemplate
            .send(orderAcceptedTopicName, event)
            .addCallback(new ListenableFutureCallback<SendResult<String, OrderAcceptedEvent>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    // TODO - Handle failures here.
                }

                @Override
                public void onSuccess(SendResult<String, OrderAcceptedEvent> eventSendResult) {
                    // TODO - Handle successes here.
                }
            });
    }
}
