package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ParkingLotServiceTest {

    ParkingLotRepository parkingLotRepository = mock(ParkingLotRepository.class);
    ParkingSpaceRepository parkingSpaceRepository = mock(ParkingSpaceRepository.class);
    UserService userService = mock(UserService.class);
    User manager = mock(User.class);
    ParkingLot parkingLot = ParkingLot.builder().id(BigInteger.valueOf(1111L)).name("parking lot 1").username("yyyyy").build();

    @Test
    public void should_save_parking_lot_when_manager_add() {

        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);
        when(manager.getRole()).thenReturn("ROLE_MANAGER");
        when(userService.findUserByName(parkingLot.getUsername())).thenReturn(manager);
        Assertions.assertEquals("parking lot 1", parkingLotRepository.save(parkingLot).getName());
    }

    @Test
    public void should_return_already_exists_when_add_duplicate_parking_lot_name() {
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 1");
        when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);
        Assertions.assertEquals(10007, parkingLotService.addParkingLot(parkingLot).getCode());
        Assertions.assertEquals("parking lot already exists", parkingLotService.addParkingLot(parkingLot).getMessage());
    }

    @Test
    public void should_save_parking_lot_when_add_different_parking_lot_name() {
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 1");
        ParkingLot curParkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 2");
        when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);
        Assertions.assertEquals(10005, parkingLotService.addParkingLot(curParkingLot).getCode());
        Assertions.assertEquals("parking lot add success", parkingLotService.addParkingLot(curParkingLot).getMessage());
    }

    @Test
    public void should_save_parking_lot_when_add_parking_lot() {
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 1");
        when(parkingLotRepository.findAll()).thenReturn(new ArrayList<>());
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);
        Assertions.assertEquals(10005, parkingLotService.addParkingLot(parkingLot).getCode());
        Assertions.assertEquals("parking lot add success", parkingLotService.addParkingLot(parkingLot).getMessage());
    }

    @Test
    public void should_delete_parking_lot_when_parking_lot_exist() {
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 1");
        when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);
        Assertions.assertEquals(10008, parkingLotService.deleteParkingLot(parkingLot.getName()).getCode());
        Assertions.assertEquals("parking lot delete success", parkingLotService.deleteParkingLot(parkingLot.getName()).getMessage());
    }

    @Test
    public void should_return_error_message_when_delete_different_parking_lot_name() {
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 1");
        ParkingLot curParkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 2");
        when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);
        Assertions.assertEquals(10009, parkingLotService.deleteParkingLot(curParkingLot.getName()).getCode());
        Assertions.assertEquals("parking lot not exist", parkingLotService.deleteParkingLot(curParkingLot.getName()).getMessage());
    }

    @Test
    public void should_delete_parking_lot_when_delete_parking_lot() {
        ParkingLot parkingLot = mock(ParkingLot.class);
        when(parkingLot.getName()).thenReturn("parking lot 1");
        when(parkingLotRepository.findAll()).thenReturn(new ArrayList<>());
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);
        Assertions.assertEquals(10009, parkingLotService.deleteParkingLot(parkingLot.getName()).getCode());
        Assertions.assertEquals("parking lot not exist", parkingLotService.deleteParkingLot(parkingLot.getName()).getMessage());
    }

}
