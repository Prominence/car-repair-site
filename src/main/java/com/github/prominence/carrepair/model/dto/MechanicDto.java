package com.github.prominence.carrepair.model.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class MechanicDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String firstName;

    @Size(max = 64)
    private String middleName;

    @NotBlank
    @Size(max = 64)
    private String lastName;

    @NotNull
    @PositiveOrZero
    private BigDecimal hourlyPayment;

    public MechanicDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getHourlyPayment() {
        return hourlyPayment;
    }

    public void setHourlyPayment(BigDecimal hourlyPayment) {
        this.hourlyPayment = hourlyPayment;
    }
}

 