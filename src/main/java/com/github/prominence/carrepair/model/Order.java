package com.github.prominence.carrepair.model;

import com.github.prominence.carrepair.enums.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @NotNull
    @Column(name = "createdOn")
    private LocalDateTime createdOn;

    @Column(name = "finishedOn")
    private LocalDateTime finishedOn;

    @Column(name = "totalPrice")
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "orderStatus")
    private String orderStatus = OrderStatus.SCHEDULED.toString();

    public Order(String description, Client client, Mechanic mechanic, LocalDateTime createdOn, LocalDateTime finishedOn, BigDecimal totalPrice, String orderStatus) {
        this.description = description;
        this.client = client;
        this.mechanic = mechanic;
        this.createdOn = createdOn;
        this.finishedOn = finishedOn;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(LocalDateTime finishedOn) {
        this.finishedOn = finishedOn;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.toString();
    }
}
