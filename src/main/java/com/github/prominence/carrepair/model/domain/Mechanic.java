package com.github.prominence.carrepair.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "mechanic")
public class Mechanic extends Person {

    @Column(name = "hourlyPayment", nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mechanic)) return false;
        if (!super.equals(o)) return false;
        Mechanic mechanic = (Mechanic) o;
        return Objects.equals(hourlyPayment, mechanic.hourlyPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hourlyPayment);
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                "hourlyPayment=" + hourlyPayment +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
