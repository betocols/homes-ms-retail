package com.tenx.ms.retail.order.utils;

import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.store.domain.StoreEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

public final class OrderConverter {
    public static Function<Order, OrderEntity> converToOrderEntity = (Order order) -> {
        OrderEntity orderE = new OrderEntity();
        StoreEntity storeE = new StoreEntity();

        storeE.setStoreId(order.getStoreId());

        orderE.setOrderId(order.getOrderId());
        orderE.setStore(storeE);
        orderE.setOrderDate(order.getOrderDate());
        orderE.setStatus(order.getStatus());
        orderE.setFirstName(order.getFirstName());
        orderE.setLastName(order.getLastName());
        orderE.setEmail(order.getEmail());
        orderE.setPhone(order.getPhone());
        orderE.setOrderItems(order.getOrderItems()
            .stream()
            .map(OrderItemConverter.convertToOrderItemEntity)
            .collect(Collectors.toList()));

        return orderE;
    };

    public static Function<OrderEntity, Order> convertToOrderDTO = (OrderEntity orderE) -> {
        Order order = new Order();

        order.setOrderId(orderE.getOrderId());
        order.setOrderDate(orderE.getOrderDate());
        order.setStatus(orderE.getStatus());
        order.setFirstName(orderE.getFirstName());
        order.setLastName(orderE.getLastName());
        order.setEmail(orderE.getEmail());
        order.setPhone(orderE.getPhone());
        order.setOrderItems(orderE.getOrderItems().stream().map(OrderItemConverter.convertToOrderItemDTO).collect(Collectors.toList()));

        return order;
    };
}
