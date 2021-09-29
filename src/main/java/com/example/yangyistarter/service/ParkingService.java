package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class ParkingService {
    ParkingSpaceRepository parkingSpaceRepository;
    ParkingLotRepository parkingLotRepository;

    public long parking(@AuthenticationPrincipal User user) {
        if (userNotLogIn() || isUserOrStupidAssistant(user)) {
            return normalParking();
        } else if (isCleverAssistant(user)) {
            return cleverParking();
        }
        return randomParking();
    }

    private boolean userNotLogIn() {
        return SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
    }

    private boolean isUserOrStupidAssistant(User user) {
        return "ROLE_USER".equals(user.getRole()) || "ROLE_STUPID_ASSISTANT".equals(user.getRole());
    }

    private boolean isCleverAssistant(User user) {
        return "ROLE_CLEVER_ASSISTANT".equals(user.getRole());
    }

    private long normalParking() {
        return selectSmallestSpace();
    }

    private long selectSmallestSpace() {
        return selectSmallestSpace(null);
    }

    private long selectSmallestSpace(String availableParkingLotName) {
        long parkingId = getSmallestParkingId(availableParkingLotName);
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(parkingId);
        return getAvailableParkingSpace(parkingId, parkingSpace);
    }

    private long getAvailableParkingSpace(long parkingId, Optional<ParkingSpace> parkingSpace) {
        if (parkingSpace.isPresent()) {
            parkingSpace.get().setIsUsed(true);
            parkingSpaceRepository.save(parkingSpace.get());
            return parkingId;
        }
        throw new IllegalArgumentException("没有合适的停车位");
    }

    private long getSmallestParkingId(String availableParkingLotName) {
        long parkingId = Long.MAX_VALUE;
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (isQualifiedSpace(availableParkingLotName, parkingSpace)) {
                parkingId = parkingSpace.getId().compareTo(parkingId) < 0 ? parkingSpace.getId() : parkingId;
            }
        }
        return parkingId;
    }

    private boolean isQualifiedSpace(String availableParkingLotName, ParkingSpace parkingSpace) {
        return !parkingSpace.getIsUsed() && (availableParkingLotName == null || availableParkingLotName.equals(parkingSpace.getParkingLotName()));
    }

    public long cleverParking() {
        Map<String, Integer> availableParkingSpaces = new HashMap<>();
        for (ParkingLot parkingLot : parkingLotRepository.findAll()) {
            countSpacesInLot(availableParkingSpaces, parkingLot);
        }
        if (availableParkingSpaces.isEmpty()) {
            throw new IllegalArgumentException("没有合适的停车位");
        }
        List<Map.Entry<String, Integer>> availableParkingSpacesList = new ArrayList<>(availableParkingSpaces.entrySet());
        availableParkingSpacesList.sort(((o1, o2) -> o2.getValue() - o1.getValue()));
        String availableParkingLotName = availableParkingSpacesList.get(0).getKey();
        return selectSmallestSpace(availableParkingLotName);
    }

    private void countSpacesInLot(Map<String, Integer> availableParkingSpaces, ParkingLot parkingLot) {
        List<Optional<ParkingSpace>> parkingSpaceList = parkingSpaceRepository.findByParkingLotName(parkingLot.getName());
        for (Optional<ParkingSpace> parkingSpace : parkingSpaceList) {
            countSpaces(availableParkingSpaces, parkingLot, parkingSpace);
        }
    }

    private void countSpaces(Map<String, Integer> availableParkingSpaces, ParkingLot parkingLot, Optional<ParkingSpace> parkingSpace) {
        if (parkingSpace.isPresent()) {
            if (!parkingSpace.get().getIsUsed()) {
                availableParkingSpaces.put(parkingLot.getName(), availableParkingSpaces.getOrDefault(parkingLot.getName(), 0) + 1);
            }
        }
    }

    public long randomParking() {
        List<ParkingSpace> availableParkingSpaceList = getParkingSpaces();
        if (availableParkingSpaceList.isEmpty()) {
            throw new IllegalArgumentException("没有合适的停车位");
        }
        int random = ThreadLocalRandom.current().nextInt(0, availableParkingSpaceList.size());
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(availableParkingSpaceList.get(random).getId());
        if (parkingSpace.isPresent()) {
            ParkingSpace curParkingSpace = parkingSpace.get();
            curParkingSpace.setIsUsed(true);
            parkingSpaceRepository.save(curParkingSpace);
            return curParkingSpace.getReceiptId();
        }
        return 0L;
    }

    private List<ParkingSpace> getParkingSpaces() {
        List<ParkingSpace> availableParkingSpaceList = new ArrayList<>();
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed()) {
                availableParkingSpaceList.add(parkingSpace);
            }
        }
        return availableParkingSpaceList;
    }
}
