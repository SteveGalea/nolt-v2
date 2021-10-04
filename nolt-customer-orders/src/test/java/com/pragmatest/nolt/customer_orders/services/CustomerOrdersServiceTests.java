package com.pragmatest.nolt.customer_orders.services;

import com.cedarsoftware.util.DeepEquals;
import com.pragmatest.nolt.customer_orders.data.entities.CustomerOrderEntity;
import com.pragmatest.nolt.customer_orders.data.repositories.CustomerOrdersRepository;
import com.pragmatest.nolt.customer_orders.services.models.Order;
import com.pragmatest.nolt.customer_orders.services.models.OrderItem;
import com.pragmatest.nolt.customer_orders.services.models.OrderSubmission;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static com.pragmatest.nolt.customer_orders.helpers.Assertions.assertIsValidUuid;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerOrdersServiceTests {

    @Autowired
    CustomerOrdersService customerOrdersService;

    @MockBean
    CustomerOrdersRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void testSubmitOrder() {
        // Arrange
        OrderSubmission orderSubmission = new OrderSubmission();
        orderSubmission.setCustomerId("00de292f-cfaf-4831-8900-6eba381517ec");
        orderSubmission.setOrderItems(List.of(new OrderItem("8b610d9f-26d4-4ef5-9153-2bc1454fc44b", 1, "")));

        CustomerOrderEntity passedCustomerOrderEntity = modelMapper.map(orderSubmission, CustomerOrderEntity.class);

        CustomerOrderEntityMatcher matcher = new CustomerOrderEntityMatcher(passedCustomerOrderEntity);

        when(repository.save(argThat(matcher)))
                .thenAnswer(I -> matcher.getMatch());

        // Act
        String id = customerOrdersService.submitOrder(orderSubmission);

        // Assert
        verify(repository, times(1)).save(argThat(matcher));

        assertNotNull(id, "Id in response is null.");
        assertIsValidUuid(id);
        assertEquals(matcher.getMatch().getOrderId(), id);
    }

    @Test
    public void testGetOrder() {

        // Arrange

        List<com.pragmatest.nolt.customer_orders.data.entities.OrderItem> entityOrderItems
                = List.of(new com.pragmatest.nolt.customer_orders.data.entities.OrderItem(
                "burger", 1, "no lettuce"
        ));

        String customerId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();

        Optional<CustomerOrderEntity> repositoryOrderEntity = Optional.of(new CustomerOrderEntity(entityOrderItems, customerId, orderId));

        List<com.pragmatest.nolt.customer_orders.services.models.OrderItem> orderItems
                = List.of(new com.pragmatest.nolt.customer_orders.services.models.OrderItem(
                "burger", 1, "no lettuce"
        ));
        Order expectedOrder = new Order(customerId, orderId, orderItems);

        when(repository.findById(orderId)).thenReturn(repositoryOrderEntity);

        // Act

        Order actualOrder = customerOrdersService.getOrder(orderId);

        // Assert

        DeepEquals.deepEquals(actualOrder, expectedOrder);
        verify(repository, times(1)).findById(orderId);
    }
}

class CustomerOrderEntityMatcher implements ArgumentMatcher<CustomerOrderEntity> {

    private CustomerOrderEntity left;
    private CustomerOrderEntity match;

    public CustomerOrderEntityMatcher(CustomerOrderEntity left) {
        this.left = left;
    }

    @Override
    public boolean matches(CustomerOrderEntity right) {
        boolean isMatch = left != null && right != null &&
                left.getCustomerId().equals(right.getCustomerId()) &&
                DeepEquals.deepEquals(left.getOrderItems(), right.getOrderItems()) &&
                isValidUUID(right.getOrderId());

        if (isMatch) {
            match = right;
        }

        return isMatch;
    }

    public CustomerOrderEntity getMatch() {
        return match;
    }

    private boolean isValidUUID(String id) {

        if (id == null) return false;

        try {
            UUID uuid = UUID.fromString(id);
            return id.equals(uuid.toString());
        } catch(IllegalArgumentException e) {
            return false;
        }
    }
}