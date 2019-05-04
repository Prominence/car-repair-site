package com.github.prominence.carrepair.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client extends Person {

    @Size(max = 32)
    @Column(name = "phoneNo")
    private String phoneNo;

    public Client(String firstName, String middleName, String lastName, String phoneNo) {
        super(firstName, middleName, lastName);
        this.phoneNo = phoneNo;
    }

    public Client() {
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(phoneNo, client.phoneNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phoneNo);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
