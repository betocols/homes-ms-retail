package com.tenx.ms.retail.product.service;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.product.utils.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    public List<Product> getAllProductsInStore(Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (storeE.isPresent()) {
            return productRepository.findByStore(storeE.get()).stream().map(entity -> ProductConverter.convertToProductDTO.apply(entity)).collect(Collectors.toList());
        }   else {
            throw new NoSuchElementException(String.format("Store with id (%d) was not found", storeId));
        }
    }

    public Product getProductInStore(Long productId, Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (!storeE.isPresent()) {
            throw new NoSuchElementException(String.format("Store with id (%d) was not found", storeId));
        }

        Optional<ProductEntity> productE = productRepository.findOneByProductIdAndStore(productId, storeE.get());
        if (productE.isPresent()) {
            return ProductConverter.convertToProductDTO.apply(productE.get());
        } else {
            throw new NoSuchElementException(String.format("Product with id (%d) was not found", productId));
        }
    }

    @Transactional
    public Long createProductInStore(Product product, Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (storeE.isPresent()) {
            product.setStoreId(storeE.get().getStoreId());
            ProductEntity productE = ProductConverter.convertToProductEntity.apply(product);
            productE = productRepository.save(productE);
            return productE.getProductId();
        } else {
            throw new NoSuchElementException(String.format("Store with id (%d) was not found", storeId));
        }
    }
}
