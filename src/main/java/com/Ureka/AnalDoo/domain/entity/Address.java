package com.Ureka.AnalDoo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    @Column(name = "local")
    private String local;
    @Column(name = "street")
    private String street;
    @Column(name = "zipcode")
    private String zipcode;
    @Column(name = "detail")
    private String detail;

    protected Address() {
    }

    public Address(String local, String street, String zipcode, String detail) {
        this.local = local;
        this.street = street;
        this.zipcode = zipcode;
        this.detail = detail;
    }
}
