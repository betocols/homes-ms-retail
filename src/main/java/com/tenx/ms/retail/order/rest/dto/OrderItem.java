package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItem {
    @ApiModelProperty(name = "Order item id")
    private Long orderItemId;

    @ApiModelProperty(name = "Product Id", required = true)
    @NotNull
    private Long productId;

    @ApiModelProperty(name = "Order item count", required = true)
    @NotNull
    @Min(0)
    private Integer count;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
