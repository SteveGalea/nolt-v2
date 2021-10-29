package com.pragmatest.nolt.customer_orders.messaging.consumers;

import com.pragmatest.nolt.customer_orders.messaging.events.OrderAcceptedEvent;
import com.pragmatest.nolt.customer_orders.services.CustomerOrdersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderAcceptedConsumer {

    @Autowired
    private CustomerOrdersService ordersService;

    @Autowired
    private ModelMapper modelMapper;

    @KafkaListener(
            topics = "${order.accepted.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderAcceptedEventKafkaListenerContainerFactory")
    public void handleOrderAccepted(OrderAcceptedEvent orderAcceptedEvent) throws Exception {
        System.out.println("Consumed message: " + orderAcceptedEvent);

        ordersService.acceptOrder(orderAcceptedEvent.getOrderId());
    }
}
