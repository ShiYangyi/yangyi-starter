package com.example.yangyistarter.repository;

import com.example.yangyistarter.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, BigInteger> {
}
