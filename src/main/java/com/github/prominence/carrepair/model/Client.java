package com.github.prominence.carrepair.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client extends Person {

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
}
