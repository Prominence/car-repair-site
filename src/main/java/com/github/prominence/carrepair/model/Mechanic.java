package com.github.prominence.carrepair.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "mechanic")
public class Mechanic extends Person {

    @NotNull
    @Column(name = "hourlyPayment")
    private BigDecimal hourlyPayment;

    public Mechanic(String firstName, String middleName, String lastName, BigDecimal hourlyPayment) {
        super(firstName, middleName, lastName);
        this.hourlyPayment = hourlyPayment;
    }

    public Mechanic() {
    }

    public BigDecimal getHourlyPayment() {
        return hourlyPayment;
    }

    public void setHourlyPayment(BigDecimal hourlyPayment) {
        this.hourlyPayment = hourlyPayment;
    }
}
