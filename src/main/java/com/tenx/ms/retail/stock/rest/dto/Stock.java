package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Stock {
    @ApiModelProperty(value = "Product Id")
    private Long productId;

    @ApiModelProperty(value = "Store Id")
    private Long storeId;

    @ApiModelProperty(value = "Product count")
    @NotNull
    @Min(0)
    private Long count;

    public Stock(Long productId, Long storeId, Long count) {
        this.productId = productId;
        this.storeId = storeId;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
