package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class Store {

    @ApiModelProperty(value = "Store Id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value="Store name", required = true)
    @NotNull
    private String name;

    public Store() {
    }

    public Store(Long storeId, String name) {
        this.storeId = storeId;
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
