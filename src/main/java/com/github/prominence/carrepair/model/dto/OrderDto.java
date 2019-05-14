package com.github.prominence.carrepair.model.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class OrderDto {

    private Long id;

    @NotNull
    @Positive
    private Long clientId;

    private String clientFirstName;
    private String clientMiddleName;
    private String clientLastName;

    @NotNull
    @Positive
    private Long mechanicId;

    private String mechanicFirstName;
    private String mechanicMiddleName;
    private String mechanicLastName;

    @Size(min = 1, max = 1024)
    private String description;

    @NotBlank
    private String orderStatus;

    @NotBlank
    private String createdOnDate;

    private String finishedOnDate;

    @NotNull
    @PositiveOrZero
    private BigDecimal totalPrice;

    public OrderDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientMiddleName() {
        return clientMiddleName;
    }

    public void setClientMiddleName(String clientMiddleName) {
        this.clientMiddleName = clientMiddleName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public Long getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(Long mechanicId) {
        this.mechanicId = mechanicId;
    }

    public String getMechanicFirstName() {
        return mechanicFirstName;
    }

    public void setMechanicFirstName(String mechanicFirstName) {
        this.mechanicFirstName = mechanicFirstName;
    }

    public String getMechanicMiddleName() {
        return mechanicMiddleName;
    }

    public void setMechanicMiddleName(String mechanicMiddleName) {
        this.mechanicMiddleName = mechanicMiddleName;
    }

    public String getMechanicLastName() {
        return mechanicLastName;
    }

    public void setMechanicLastName(String mechanicLastName) {
        this.mechanicLastName = mechanicLastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedOnDate() {
        return createdOnDate;
    }

    public void setCreatedOnDate(String createdOnDate) {
        this.createdOnDate = createdOnDate;
    }

    public String getFinishedOnDate() {
        return finishedOnDate;
    }

    public void setFinishedOnDate(String finishedOnDate) {
        this.finishedOnDate = finishedOnDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
