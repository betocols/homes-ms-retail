package com.tenx.ms.retail.stock.utils;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.domain.StoreEntity;

import java.util.function.Function;

public final class StockConverter {
    public static Function<Stock, StockEntity> convertToStockEntity = (Stock stock) -> {
        ProductEntity productEntity = new ProductEntity();
        StoreEntity storeEntity = new StoreEntity();
        StockEntity stockEntity = new StockEntity();

        productEntity.setProductId(stock.getProductId());
        storeEntity.setStoreId(stock.getStoreId());

        if (stock.getProductId() != null) {
            stockEntity.setProductId(stock.getProductId());
        }

        stockEntity.setProduct(productEntity);
        stockEntity.setStore(storeEntity);
        stockEntity.setCount(stock.getCount());

        return stockEntity;
    };

    public static Function<StockEntity, Stock> convertToStockDTO = (StockEntity stockEntity) -> {
        if ( stockEntity == null ){
            return null;
        }
        Stock stock = new Stock();

        stock.setStoreId(stockEntity.getStore().getStoreId());
        stock.setProductId(stockEntity.getProduct().getProductId());
        stock.setCount(stockEntity.getCount());

        return stock;
    };
}
