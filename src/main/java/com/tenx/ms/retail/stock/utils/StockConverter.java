package com.tenx.ms.retail.stock.utils;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.domain.StoreEntity;

public class StockConverter {
    public StockEntity convertToStockEntity(Stock stock, ProductEntity productE, StoreEntity storeE) {
        return new StockEntity(stock.getProductId(), productE, storeE, stock.getCount());
    }

    public Stock convertToStockDTO(StockEntity stockEntity) {
        return new Stock(stockEntity.getProductId(), stockEntity.getStore().getStoreId(), stockEntity.getCount());
    }
}
