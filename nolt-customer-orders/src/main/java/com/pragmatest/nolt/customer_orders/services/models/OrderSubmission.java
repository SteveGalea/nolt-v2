package com.pragmatest.nolt.customer_orders.services.models;

import java.util.List;

public class OrderSubmission {

    private List<OrderItem> orderItems;
    private String customerId;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
