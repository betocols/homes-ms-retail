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

    public Stock getStockByStockAndProductId(Long storeId, Long productId) {
        Optional<StockEntity> stockE = stockRepository.findOneByStoreIdAndProductId(storeId, productId);
        if (stockE.isPresent()) {
            return StockConverter.convertToStockDTO.apply(stockE.get());
        } else {
            throw new NoSuchElementException(String.format("Stock with store id (%d) and product id (%d) was not found", storeId, productId));

        }
    }

    @Transactional
    public void upsertProductStock(Stock stock) {
        Long storeId = stock.getStoreId();
        Long productId = stock.getProductId();

        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (!storeE.isPresent()) {
            throw new NoSuchElementException(String.format("Store with id (%d) was not found", storeId));
        }

        Optional<ProductEntity> productE = productRepository.findOneByProductIdAndStore(productId, storeE.get());
        if (!productE.isPresent()) {
            throw new NoSuchElementException(String.format("Product with id (%d) was not found", productId));
        }

        stockRepository.save(StockConverter.convertToStockEntity.apply(stock));
    }
}
