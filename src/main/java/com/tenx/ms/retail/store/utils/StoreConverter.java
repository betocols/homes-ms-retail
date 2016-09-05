package com.tenx.ms.retail.store.utils;

import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter {
    public StoreEntity convertToStoreEntity(Store store) {
        return new StoreEntity(store.getStoreId(), store.getName());
    }

    public Store convertToStoreDTO(StoreEntity storeEntity) {
        return new Store(storeEntity.getStoreId(), storeEntity.getName());
    }
}
