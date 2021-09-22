package com.example.yangyistarter.repository;

import com.example.yangyistarter.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, BigInteger> {

}
