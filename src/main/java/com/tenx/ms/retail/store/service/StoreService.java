package com.tenx.ms.retail.store.service;

import com.tenx.ms.retail.store.utils.StoreConverter;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreConverter storeConverter;

    public List<Store> getStores() {
        return storeRepository.findAll().stream().map(entity -> storeConverter.convertToStoreDTO(entity)).collect(Collectors.toList());
    }

    public Store getStoreById(Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (storeE.isPresent()) {
            return storeConverter.convertToStoreDTO(storeE.get());
        }  else {
            throw new NoSuchElementException("Store was not found.");
        }

    }

    @Transactional
    public Long create(Store store) {
        StoreEntity storeE = storeConverter.convertToStoreEntity(store);
        storeE = storeRepository.save(storeE);

        return storeE.getStoreId();
    }
}
