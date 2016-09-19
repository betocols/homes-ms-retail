package com.tenx.ms.retail.stock.repository;

import com.tenx.ms.retail.stock.domain.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long>{
    Optional<StockEntity> findOneByStoreIdAndProductId(Long storeId, Long productId);
    List<StockEntity> findByStoreId(Long storeId);
}
