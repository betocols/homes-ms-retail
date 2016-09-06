package com.tenx.ms.retail.product.domain;

import com.tenx.ms.retail.order.domain.OrderItemEntity;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.store.domain.StoreEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="product")
public class ProductEntity {
    @Id
    @GeneratedValue
    @Column(name="product_id", nullable = false)
    private Long productId;

    @ManyToOne
    @JoinColumn(name="store_id")
    private StoreEntity store;

    @OneToOne
    @JoinColumn(name = "product_id")
    private StockEntity stock;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Size(min = 5, max = 10)
    @Pattern(regexp = "[A-Za-z0-9]*")
    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "price", precision = 20, scale = 2, nullable = false)
    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    private List<OrderItemEntity> orderItems;

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public StockEntity getStock() {
        return stock;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }
}
