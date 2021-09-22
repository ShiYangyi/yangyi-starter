package com.example.yangyistarter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "parking_space")
@Builder
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    //在程序里面使用驼峰命令，在数据库中使用_以及小写
    @Column(name = "is_used")
    private Boolean isUsed;
    @Column(name = "receipt_id")
    private BigInteger receiptId;
    @Column(name = "parking_lot_name")
    private String parkingLotName;
}
