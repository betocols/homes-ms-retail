package com.tenx.ms.retail.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "store")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    public StoreEntity() {
    }

    public StoreEntity(String name) {
        this.name = name;
    }

    public StoreEntity(Long storeId, String name) {
        this.storeId = storeId;
        this.name = name;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
