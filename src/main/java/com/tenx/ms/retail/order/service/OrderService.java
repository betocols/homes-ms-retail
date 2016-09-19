package com.tenx.ms.retail.order.service;

import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.domain.enums.OrderStatusEnum;
import com.tenx.ms.retail.order.repository.OrderRepository;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.order.utils.OrderConverter;
import com.tenx.ms.retail.order.utils.OrderItemConverter;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.service.StockService;
import com.tenx.ms.retail.stock.utils.StockConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    public Order createOrderForStore(Order order) {
        OrderEntity orderE = OrderConverter.converToOrderEntity.apply(order);
        Order orderDTO;
        List<OrderItem> orderedItems = new ArrayList<>();
        List<OrderItem> backOrderedItems = new ArrayList<>();

        if (order.getOrderDate() == null) {
            orderE.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        }

        if (order.getStatus() == null) {
            orderE.setStatus(OrderStatusEnum.ORDERED);
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Optional<StockEntity> stockE = stockRepository.findOneByStoreIdAndProductId(order.getStoreId(), orderItem.getProductId());

            if (stockE.isPresent() && stockE.get().getCount() >= orderItem.getCount()) {
                orderedItems.add(orderItem);
                stockE.get().setCount(stockE.get().getCount() - orderItem.getCount());
                stockService.upsertProductStock(StockConverter.convertToStockDTO.apply(stockE.get()));
            } else {
                backOrderedItems.add(orderItem);
            }
        }
        orderE.setOrderItems(orderedItems.stream().map(OrderItemConverter.convertToOrderItemEntity).collect(Collectors.toList()));
        orderE = orderRepository.save(orderE);

        orderDTO = OrderConverter.convertToOrderDTO.apply(orderE);
        orderDTO.setBackOrderedItems(backOrderedItems);

        return orderDTO;
    }
}
