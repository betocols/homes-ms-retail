package com.tenx.ms.retail.product.utils;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.domain.StoreEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public final class ProductConverter {
    public static Function<Product, ProductEntity> convertToProductEntity = (Product product) -> {
        ProductEntity productE = new ProductEntity();
        StoreEntity storeE = new StoreEntity();

        storeE.setStoreId(product.getStoreId());

        productE.setProductId(product.getProductId());
        productE.setStore(storeE);
        productE.setName(product.getName());
        productE.setDescription(product.getDescription());
        productE.setSku(product.getSku());
        productE.setPrice(product.getPrice());

        return productE;
    };

    public static Function<ProductEntity, Product> convertToProductDTO = (ProductEntity productE) -> {
        Product product = new Product();

        product.setProductId(productE.getProductId());
        product.setStoreId(productE.getStore().getStoreId());
        product.setName(productE.getName());
        product.setDescription(productE.getDescription());
        product.setSku(productE.getSku());
        product.setPrice(productE.getPrice());

        return product;
    };
}
