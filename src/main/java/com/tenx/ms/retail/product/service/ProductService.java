package com.tenx.ms.retail.product.service;

import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.utils.ProductConverter;
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

    @Autowired
    private ProductConverter productConverter;

    public List<Product> getAllProductsInStore(Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (storeE.isPresent()) {
            return productRepository.findByStore(storeE.get()).stream().map(entity -> productConverter.convertToProductDTO(entity)).collect(Collectors.toList());
        }   else {
            throw new NoSuchElementException("Store was not found.");
        }
    }

    public Product getProductInStore(Long productId, Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (!storeE.isPresent()) {
            throw new NoSuchElementException("Store was not found.");
        }

        Optional<ProductEntity> productE = productRepository.findOneByProductIdAndStore(productId, storeE.get());
        if (productE.isPresent()) {
            return productConverter.convertToProductDTO(productE.get());
        } else {
            throw new NoSuchElementException("Product was not found in store.");
        }
    }

    @Transactional
    public Long createProductInStore(Product product, Long storeId) {
        Optional<StoreEntity> storeE = storeRepository.findOneByStoreId(storeId);
        if (storeE.isPresent()) {
            ProductEntity productE = productConverter.convertToProductEntity(product, storeE.get());
            productE = productRepository.save(productE);
            return productE.getProductId();
        } else {
            throw new NoSuchElementException("Store was not found.");
        }
    }
}
