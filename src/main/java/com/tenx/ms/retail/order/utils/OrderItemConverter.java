package com.tenx.ms.retail.order.utils;

import com.tenx.ms.retail.order.domain.OrderItemEntity;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.product.domain.ProductEntity;

import java.util.function.Function;

public final class OrderItemConverter {

    public static Function<OrderItem, OrderItemEntity> convertToOrderItemEntity = (OrderItem orderItem) -> {
        OrderItemEntity orderItemE = new OrderItemEntity();
        ProductEntity productE = new ProductEntity();

        productE.setProductId(orderItem.getProductId());

        orderItemE.setOrderItemId(orderItem.getOrderItemId());
        orderItemE.setProductId(orderItem.getProductId());
        orderItemE.setProduct(productE);
        orderItemE.setCount(orderItem.getCount());

        return orderItemE;
    };

    public static Function<OrderItemEntity, OrderItem> convertToOrderItemDTO = (OrderItemEntity orderItemE) -> {
        OrderItem orderItem = new OrderItem();

        orderItem.setOrderItemId(orderItemE.getOrderItemId());
        orderItem.setProductId(orderItemE.getProductId());
        orderItem.setCount(orderItemE.getCount());

        return orderItem;
    };
}
