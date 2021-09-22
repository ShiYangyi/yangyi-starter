package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ParkingService {

    ParkingSpaceRepository parkingSpaceRepository;
    ParkingLotRepository parkingLotRepository;

    public BigInteger randomParking() {
        BigInteger minId = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger maxId = BigInteger.valueOf(Long.MIN_VALUE);
        int avaiableSum = 0;
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed()) {
                avaiableSum++;
            }
            minId = parkingSpace.getId().compareTo(minId) < 0 ? parkingSpace.getId() : minId;
            maxId = parkingSpace.getId().compareTo(maxId) < 0 ? maxId : parkingSpace.getId();
        }
        if(avaiableSum == 0) {
            throw new IllegalArgumentException("没有合适的停车位");
        }
        Random random = new Random();
        Optional<ParkingSpace> parkingSpace = Optional.empty();
        int randomId = random.nextInt(maxId.intValue() - minId.intValue()) + minId.intValue();
        parkingSpace = parkingSpaceRepository.findById(BigInteger.valueOf(randomId));
        while(parkingSpace.isPresent() && !parkingSpace.get().getIsUsed()) {
            parkingSpace.get().setIsUsed(true);
            parkingSpaceRepository.save(parkingSpace.get());
            return BigInteger.valueOf(randomId);
        }
        throw new IllegalArgumentException("没有合适的停车位");
    }

    public BigInteger cleverParking() {
        Map<String, Integer> avaiableParkingSpaces = new HashMap<>();
        for (ParkingLot parkingLot : parkingLotRepository.findAll()) {
            for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
                if (!parkingSpace.getIsUsed()) {
                    avaiableParkingSpaces.put(parkingLot.getName(), avaiableParkingSpaces.getOrDefault(parkingLot.getName(), 0) + 1);
                }
            }
        }
        List<Map.Entry<String, Integer>> avaiableParkingSpacesList = new ArrayList<>(avaiableParkingSpaces.entrySet());
        Collections.sort(avaiableParkingSpacesList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //按照value值从大到小排序
                return (o2.getValue() - o1.getValue());
            }
        });
        String avaiableParkingLotName = avaiableParkingSpacesList.get(0).getKey();
        BigInteger parkingId = BigInteger.valueOf(Long.MAX_VALUE);
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed() && avaiableParkingLotName.equals(parkingSpace.getParkingLotName())) {
                parkingId = parkingSpace.getId().compareTo(parkingId) < 0 ? parkingSpace.getId() : parkingId;
            }
        }
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(parkingId);
        if (parkingSpace.isPresent()) {
            parkingSpace.get().setIsUsed(true);
            parkingSpaceRepository.save(parkingSpace.get());
            return parkingId;
        }
        throw new IllegalArgumentException("没有合适的停车位");
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
            return parkingId;
        }
        throw new IllegalArgumentException("没有合适的停车位");
    }
}
