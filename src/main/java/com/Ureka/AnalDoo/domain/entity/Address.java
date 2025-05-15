package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.domain.entity.enums.Local;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    @Column(name = "local")
    @Enumerated(value = EnumType.STRING)
    private Local local;
    @Column(name = "street")
    private String street;
    @Column(name = "zipcode")
    private String zipcode;
    @Column(name = "detail")
    private String detail;

    protected Address() {
    }

    public Address(Local local, String street, String zipcode, String detail) {
        this.local = local;
        this.street = street;
        this.zipcode = zipcode;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return  local.getName() + " " + street + " " + detail + " ";
    }
}
