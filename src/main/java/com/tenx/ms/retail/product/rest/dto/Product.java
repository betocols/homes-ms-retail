package com.tenx.ms.retail.product.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("Product")
public class Product {
    @ApiModelProperty(value = "Product id", readOnly = true)
    private Long productId;

    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Product name")
    @NotNull
    private String name;

    @ApiModelProperty(value = "Product description")
    @NotNull
    private String description;

    @ApiModelProperty(value = "Product sku")
    @NotNull
    private String sku;

    @ApiModelProperty(value = "Product price")
    @NotNull
    private BigDecimal price;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
