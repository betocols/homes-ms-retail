package com.tenx.ms.retail.stock.service;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.utils.StockConverter;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockConverter stockConverter;

    @Transactional
    public void upsertProductStock(Stock stock) {
        Long storeId = stock.getStoreId();
        Long productId = stock.getProductId();
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        Optional<ProductEntity> productE = productRepository.findOneByProductIdAndStoreId(productId, storeId);

        if (!storeE.isPresent()) {
            throw new NoSuchElementException("Store was not found");
        }

        if (!productE.isPresent()) {
            throw new NoSuchElementException("Product was not found");
        }

        stockRepository.save(stockConverter.convertToStockEntity(stock, productE.get(), storeE.get()));
    }
}
