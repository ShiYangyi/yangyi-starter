package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import lombok.AllArgsConstructor;
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

    //未登陆
    private boolean userNotLogIn() {
        //return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != user;
        //运行这句代码报了空指针异常，断点调试，到这一步，计算SecurityContextHolder.getContext().getAuthentication()的值发现是null。计算SecurityContextHolder.getContext()不为空。
        return SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null;
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
        long parkingId = Long.MAX_VALUE;
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed() && (availableParkingLotName == null || availableParkingLotName.equals(parkingSpace.getParkingLotName()))) {
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

    public long cleverParking() {
        Map<String, Integer> availableParkingSpaces = new HashMap<>();
        for (ParkingLot parkingLot : parkingLotRepository.findAll()) {
            countSpacesInLot(availableParkingSpaces, parkingLot);
        }
        if (availableParkingSpaces.isEmpty()) {
            throw new IllegalArgumentException("没有合适的停车位");
        }
        List<Map.Entry<String, Integer>> avaiableParkingSpacesList = new ArrayList<>(availableParkingSpaces.entrySet());
        /*Collections.sort(avaiableParkingSpacesList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //按照value值从大到小排序
                return (o2.getValue() - o1.getValue());
            }
        });*/
        avaiableParkingSpacesList.sort(((o1, o2) -> o2.getValue() - o1.getValue()));
        //avaiableParkingSpacesList.sort(Comparator.comparingLong(ParkingSpace::getValue));
        String avaiableParkingLotName = avaiableParkingSpacesList.get(0).getKey();
        return selectSmallestSpace(avaiableParkingLotName);
    }

    private void countSpacesInLot(Map<String, Integer> avaiableParkingSpaces, ParkingLot parkingLot) {
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed()) {
                avaiableParkingSpaces.put(parkingLot.getName(), avaiableParkingSpaces.getOrDefault(parkingLot.getName(), 0) + 1);
            }
        }
    }

    public long randomParking() {
        List<ParkingSpace> avaiableParkingSpaceList = getParkingSpaces();
        if (avaiableParkingSpaceList.isEmpty()) {
            throw new IllegalArgumentException("没有合适的停车位");
        }
        /*不使用Random类，改为使用ThreadLocalRandom.current().nextInt(min,max)
        Random random = new Random();
        int randomId = random.nextInt(maxId.intValue() - minId.intValue()) + minId.intValue();*/

        //ThreadLocalRandom.current().nextInt(a,b)表示随机数包含a，但不包含b
        //int randomId = ThreadLocalRandom.current().nextInt(minId.intValue(), maxId.intValue()+1);

        /*while (!(parkingSpace.isPresent() && !parkingSpace.get().getIsUsed())) {
            randomId = ThreadLocalRandom.current().nextInt(minId.intValue(), maxId.intValue() + 1);
            parkingSpace = parkingSpaceRepository.findById(BigInteger.valueOf(randomId));
        }*/
        //由于这种实现会造成很多浪费，得到的随机数对应的数据可能是不存在的，为了避免浪费，应该将符合要求的数据先统一存储在集合里，然后再在该集合里随机选取数据

        int random = ThreadLocalRandom.current().nextInt(0, avaiableParkingSpaceList.size());
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(avaiableParkingSpaceList.get(random).getId());
        parkingSpace.get().setIsUsed(true);
        parkingSpaceRepository.save(parkingSpace.get());
        return parkingSpace.get().getReceiptId();
    }

    private List<ParkingSpace> getParkingSpaces() {
        List<ParkingSpace> avaiableParkingSpaceList = new ArrayList<>();
        //由于这种实现会造成很多浪费，得到的随机数对应的数据可能是不存在的，为了避免浪费，应该将符合要求的数据先统一存储在集合里，然后再在该集合里随机选取数据
        for (ParkingSpace parkingSpace : parkingSpaceRepository.findAll()) {
            if (!parkingSpace.getIsUsed()) {
                avaiableParkingSpaceList.add(parkingSpace);
            }
        }
        return avaiableParkingSpaceList;
    }
}
