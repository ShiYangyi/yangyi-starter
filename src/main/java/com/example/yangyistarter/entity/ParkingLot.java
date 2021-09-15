package com.example.yangyistarter.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;

    @Override
    public String toString() {
        //Request{name='syy', password='1'},这是原来toString()的格式
        //{"name": "po", "password": "0"}，重写后的格式
        String result = "{\"name\": \"" + name + '\"' + ", \"username\": \"" + username + '\"' + '}';
        return result;
    }

}
