package com.example.yangyistarter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigInteger;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private BigInteger id;
    private String name;
    private String password;
}
