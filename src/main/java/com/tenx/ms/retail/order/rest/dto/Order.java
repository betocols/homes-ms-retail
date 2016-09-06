package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.validation.constraints.Email;
import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import com.tenx.ms.retail.order.domain.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    @ApiModelProperty(value = "Order id", readOnly = true)
    private Long orderId;

    @ApiModelProperty(value = "Store id", readOnly = true)
    private Long storeId;

    @ApiModelProperty(value = "Order date", readOnly = true)
    private Timestamp orderDate;

    @ApiModelProperty(value = "Order status")
    private OrderStatusEnum status;

    @ApiModelProperty(value = "Order product items")
    @NotNull
    private List<OrderItem> orderItems;

    @ApiModelProperty(value = "Back ordered products items")
    private List<OrderItem> backOrderedItems;

    @ApiModelProperty(value = "First name of order buyer")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only alpha characters")
    @NotBlank
    private String firstName;

    @ApiModelProperty(value = "Last name of order buyer")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only alpha characters")
    @NotBlank
    private String lastName;

    @ApiModelProperty(value = "Email of order buyer")
    @Email
    @NotBlank
    private String email;

    @ApiModelProperty(value = "Phone of order buyer")
    @PhoneNumber
    @NotBlank
    private String phone;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getBackOrderedItems() {
        return backOrderedItems;
    }

    public void setBackOrderedItems(List<OrderItem> backOrderedItems) {
        this.backOrderedItems = backOrderedItems;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
