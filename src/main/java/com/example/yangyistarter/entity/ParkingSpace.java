package com.example.yangyistarter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parking_space")
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_used")
    private Boolean isUsed;
    @Column(name = "receipt_id")
    private Long receiptId;
    @Column(name = "parking_lot_name")
    private String parkingLotName;
}
