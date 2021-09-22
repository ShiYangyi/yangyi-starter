package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingService {

    ParkingSpaceRepository parkingSpaceRepository;

    public BigInteger randomParking() {

        return null;
    }

    public BigInteger cleverParking() {
        return null;
    }

    public BigInteger parking() {
        BigInteger parkingId = BigInteger.valueOf(Long.MAX_VALUE);
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed()) {
                parkingId = parkingSpace.getId().compareTo(parkingId) < 0 ? parkingSpace.getId() : parkingId;
            }
        }
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(parkingId);
        if (parkingSpace.isPresent()) {
            parkingSpace.get().setIsUsed(true);
            //没有存库
            parkingSpaceRepository.save(parkingSpace.get());
        }
        return parkingId;
    }
}
