package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ParkingLotServiceTest {

    ParkingLotRepository parkingLotRepository = mock(ParkingLotRepository.class);
    ParkingSpaceRepository parkingSpaceRepository = mock(ParkingSpaceRepository.class);
    UserService userService = mock(UserService.class);
    User manager = mock(User.class);
    ParkingLot curParkingLot = mock(ParkingLot.class);
    ParkingSpace parkingSpace = mock(ParkingSpace.class);
    ParkingLot parkingLot = ParkingLot.builder().id(1111L).name("parking lot 1").username("yyyyy").build();
    ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository, parkingSpaceRepository, userService);

    @Test
    public void should_save_parking_lot_when_manager_add() {
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);
        when(userService.findUserByName(parkingLot.getUsername())).thenReturn(manager);
        Assertions.assertEquals("parking lot 1", parkingLotRepository.save(parkingLot).getName());
    }

    @Test
    public void should_return_already_exists_when_add_duplicate_parking_lot_name() {
        when(curParkingLot.getName()).thenReturn("parking lot 1");
        when(parkingLotRepository.findByName("parking lot 1")).thenReturn(Optional.of(parkingLot));
        Assertions.assertEquals(10007, parkingLotService.addParkingLot(curParkingLot).getCode());
        Assertions.assertEquals("parking lot already exists", parkingLotService.addParkingLot(curParkingLot).getMessage());
    }

    @Test
    public void should_save_parking_lot_when_add_different_parking_lot_name() {
        when(parkingLotRepository.findByName("parking lot 1")).thenReturn(Optional.empty());
        Assertions.assertEquals(10005, parkingLotService.addParkingLot(parkingLot).getCode());
        Assertions.assertEquals("parking lot add success", parkingLotService.addParkingLot(parkingLot).getMessage());
    }

    @Test
    public void should_save_parking_lot_when_add_parking_lot() {
        when(parkingLotRepository.findByName("parking lot 1")).thenReturn(Optional.empty());
        Assertions.assertEquals(10005, parkingLotService.addParkingLot(parkingLot).getCode());
        Assertions.assertEquals("parking lot add success", parkingLotService.addParkingLot(parkingLot).getMessage());
    }

    @Test
    public void should_delete_parking_lot_when_parking_lot_exist() {
        when(parkingLotRepository.findByName("parking lot 1")).thenReturn(Optional.of(parkingLot));
        when(parkingSpace.getParkingLotName()).thenReturn("parking lot 1");
        when(parkingSpaceRepository.findByParkingLotName("parking lot 1")).thenReturn(Collections.singletonList(Optional.of(parkingSpace)));
        Assertions.assertEquals(10008, parkingLotService.deleteParkingLot(parkingLot.getName()).getCode());
        Assertions.assertEquals("parking lot delete success", parkingLotService.deleteParkingLot(parkingLot.getName()).getMessage());
    }

    @Test
    public void should_return_error_message_when_delete_different_parking_lot_name() {
        when(parkingLotRepository.findByName("parking lot 1")).thenReturn(Optional.empty());
        Assertions.assertEquals(10009, parkingLotService.deleteParkingLot(parkingLot.getName()).getCode());
        Assertions.assertEquals("parking lot not exist", parkingLotService.deleteParkingLot(parkingLot.getName()).getMessage());
    }
}
