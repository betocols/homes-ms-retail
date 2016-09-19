package com.tenx.ms.retail.store.utils;

import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public final class StoreConverter {
    public static Function<Store, StoreEntity> convertToStoreEntity = (Store store) -> {
        StoreEntity storeE = new StoreEntity();

        if (store.getStoreId() != null) {
            storeE.setStoreId(store.getStoreId());
        }
        storeE.setName(store.getName());

        return storeE;
    };

    public static Function<StoreEntity, Store> convertToStoreDTO = (StoreEntity storeE) -> {
        Store store = new Store();

        store.setStoreId(storeE.getStoreId());
        store.setName(storeE.getName());

        return store;
    };
}
