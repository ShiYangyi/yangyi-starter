package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.ParkingLotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ParkingLotServiceTest {

    ParkingLotRepository parkingLotRepository = mock(ParkingLotRepository.class);
    UserService userService = mock(UserService.class);
    User manager = mock(User.class);
    User assistant = mock(User.class);
    User user = mock(User.class);

    ParkingLot parkingLot = ParkingLot.builder().id(BigInteger.valueOf(1111L)).name("parking lot 1").username("yyyyy").build();

    @Test
    public void should_save_parking_lot_when_manager_add() {

        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);
        when(manager.getRole()).thenReturn("ROLE_MANAGER");
        when(userService.findUserByName(parkingLot.getUsername())).thenReturn(manager);
        Assertions.assertEquals("parking lot 1", parkingLotRepository.save(parkingLot).getName());
    }

}
