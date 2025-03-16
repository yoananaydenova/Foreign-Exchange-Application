package com.yoanan.foreignexchangeapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="codes")
public class CurrencyCodeEntity {

    private String code;
    private String name;


    public CurrencyCodeEntity() {
    }

    public CurrencyCodeEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Id
    @Column(name="code", nullable = false, updatable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name="name", nullable = false, updatable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
