package com.example.yangyistarter.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "parking_space")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "isUsed")
    private Boolean isUsed;
    @Column(name = "receipt")
    private String receipt;
    @Column(name = "parkingLotName")
    private BigInteger parkingLotName;

}
