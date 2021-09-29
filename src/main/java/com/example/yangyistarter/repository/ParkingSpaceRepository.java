package com.example.yangyistarter.repository;

import com.example.yangyistarter.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<Optional<ParkingSpace>> findByParkingLotName(String parkingLotName);
}
