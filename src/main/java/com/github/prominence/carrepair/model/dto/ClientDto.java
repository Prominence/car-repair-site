package com.github.prominence.carrepair.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ClientDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String firstName;

    @Size(max = 64)
    private String middleName;

    @NotBlank
    @Size(max = 64)
    private String lastName;

    @Size(max = 32)
    private String phone;

    public ClientDto() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
