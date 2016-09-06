package com.tenx.ms.retail.product.repository;


import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findOneByProductIdAndStore(Long id, StoreEntity store);
    List<ProductEntity> findByStore(StoreEntity store);
}
