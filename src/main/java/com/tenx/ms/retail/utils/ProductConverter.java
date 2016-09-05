package com.tenx.ms.retail.utils;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductEntity convertToProductEntity(Product product, StoreEntity store) {
        return new ProductEntity(product.getProductId(), store, product.getName(), product.getDescription(),
                                 product.getSku(), product.getPrice());
    }

    public Product convertToProductDTO(ProductEntity productEntity) {
        return new Product(productEntity.getProductId(), productEntity.getStore().getStoreId(), productEntity.getName(),
                           productEntity.getDescription(), productEntity.getSku(), productEntity.getPrice());
    }
}
