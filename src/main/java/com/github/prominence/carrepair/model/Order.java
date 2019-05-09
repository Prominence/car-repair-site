package com.github.prominence.carrepair.model;

import com.github.prominence.carrepair.enums.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 1024)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER) // change to LAZY after DTO implementation
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER) // change to LAZY after DTO implementation
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @NotNull
    @Column(name = "createdOn")
    private LocalDateTime createdOn;

    @Column(name = "finishedOn")
    private LocalDateTime finishedOn;

    @Min(value = 0)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(description, order.description) &&
                Objects.equals(client, order.client) &&
                Objects.equals(mechanic, order.mechanic) &&
                Objects.equals(createdOn, order.createdOn) &&
                Objects.equals(finishedOn, order.finishedOn) &&
                Objects.equals(totalPrice, order.totalPrice) &&
                Objects.equals(orderStatus, order.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, client, mechanic, createdOn, finishedOn, totalPrice, orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", client=" + client +
                ", mechanic=" + mechanic +
                ", createdOn=" + createdOn +
                ", finishedOn=" + finishedOn +
                ", totalPrice=" + totalPrice +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
