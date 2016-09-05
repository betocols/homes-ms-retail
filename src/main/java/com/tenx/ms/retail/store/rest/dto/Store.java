package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModelProperty;

public class Store {

    @ApiModelProperty(value = "Store Id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value="Store name", required = true)
    private String name;

    public Store() {
    }

    public Store(String name) {
        this.name = name;
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
}
