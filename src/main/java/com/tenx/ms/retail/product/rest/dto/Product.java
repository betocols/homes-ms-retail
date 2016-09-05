package com.tenx.ms.retail.product.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel("Product")
public class Product {
    @ApiModelProperty(value = "Product id", readOnly = true)
    private Long productId;

    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Product name", required = true)
    private String name;

    @ApiModelProperty(value = "Product description", required = true)
    private String description;

    @ApiModelProperty(value = "Product sku", required = true)
    private String sku;

    @ApiModelProperty(value = "Product price", required = true)
    private BigDecimal price;

    public Product() {
    }

    public Product(Long productId, Long storeId, String name, String description, String sku, BigDecimal price) {
        this.productId = productId;
        this.storeId = storeId;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
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
