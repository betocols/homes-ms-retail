package com.tenx.ms.retail.store;

import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter {
    public StoreEntity convertToStoreEntity(Store store) {
        StoreEntity storeEntity = new StoreEntity();

        if (store != null) {
            storeEntity.setName(store.getName());
        }

        return storeEntity;
    }

    public Store convertToStoreDTO(StoreEntity storeEntity) {
        Store store = new Store();

        if (storeEntity != null) {
            store.setStoreId(storeEntity.getStoreId());
            store.setName(storeEntity.getName());
        }

        return store;
    }
}
