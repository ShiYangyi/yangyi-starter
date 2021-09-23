package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ParkingServiceTest {

    ParkingSpaceRepository parkingSpaceRepository = mock(ParkingSpaceRepository.class);
    ParkingLotRepository parkingLotRepository = mock(ParkingLotRepository.class);
    ParkingService parkingService = new ParkingService(parkingSpaceRepository, parkingLotRepository);

    ParkingSpace parkingSpace1 = ParkingSpace.builder().id(BigInteger.valueOf(11L)).receiptId(BigInteger.valueOf(11L)).isUsed(false).parkingLotName("parking_lot_1").build();
    ParkingSpace parkingSpace2 = ParkingSpace.builder().id(BigInteger.valueOf(12L)).receiptId(BigInteger.valueOf(12L)).isUsed(false).parkingLotName("parking_lot_1").build();
    ParkingSpace parkingSpace3 = ParkingSpace.builder().id(BigInteger.valueOf(13L)).receiptId(BigInteger.valueOf(13L)).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace4 = ParkingSpace.builder().id(BigInteger.valueOf(14L)).receiptId(BigInteger.valueOf(14L)).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace5 = ParkingSpace.builder().id(BigInteger.valueOf(15L)).receiptId(BigInteger.valueOf(15L)).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace6 = ParkingSpace.builder().id(BigInteger.valueOf(16L)).receiptId(BigInteger.valueOf(16L)).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace7 = ParkingSpace.builder().id(BigInteger.valueOf(17L)).receiptId(BigInteger.valueOf(17L)).isUsed(true).parkingLotName("parking_lot_1").build();
    ParkingSpace parkingSpace8 = ParkingSpace.builder().id(BigInteger.valueOf(18L)).receiptId(BigInteger.valueOf(18L)).isUsed(true).parkingLotName("parking_lot_2").build();

    ParkingLot parkingLot1 = ParkingLot.builder().id(BigInteger.valueOf(111L)).name("parking_lot_1").username("user1").build();
    ParkingLot parkingLot2 = ParkingLot.builder().id(BigInteger.valueOf(112L)).name("parking_lot_2").username("user2").build();

    @Test
    public void should_return_available_receipt_id_when_available_parking_space_exist() {

        //given

        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace7, parkingSpace7));
        when(parkingSpaceRepository.findById(BigInteger.valueOf(11L))).thenReturn(Optional.of(parkingSpace1));

        //then
        Assertions.assertEquals(BigInteger.valueOf(11L), parkingService.parking());
    }

    @Test
    public void should_throw_error_when_unavailable_parking_space() {
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        //下面方法的使用，第一个参数是异常类，所以写.class，第二个参数写可执行的方法，所以写成下面这种表达式的形式。并且这条验证语句的返回值中就可以获取到抛出的异常信息
        /*Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());
        IllegalArgumentException err = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());*/
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_return_available_receipt_id_when_clever_and_available_parking_space_exist() {
        //given

        //when
        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace4, parkingSpace5, parkingSpace6, parkingSpace7, parkingSpace8));
        when(parkingSpaceRepository.findById(BigInteger.valueOf(13L))).thenReturn(Optional.of(parkingSpace3));

        //then
        Assertions.assertEquals(BigInteger.valueOf(13L), parkingService.cleverParking());

    }

    @Test
    public void should_throw_error_when_clever_parking_lot_empty() {

        //given

        //when
        when(parkingLotRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.cleverParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_throw_error_when_clever_parking_space_unavailable() {
        //given

        //when
        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.cleverParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_throw_error_when_clever_parking_space_not_exist() {
        //given

        //when
        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace4, parkingSpace5, parkingSpace6, parkingSpace7, parkingSpace8));
        when(parkingSpaceRepository.findById(BigInteger.valueOf(13L))).thenReturn(Optional.empty());

        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.cleverParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_return_available_receipt_id_when_random_and_available_parking_space_exist() {

        //given
        //不需要对随机数的生成方法进行mock，因为对我的测试无影响，可以在findById()方法中传入参数为anyInteger()
        //ThreadLocalRandom threadLocalRandom = mock(ThreadLocalRandom.class);

        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace4, parkingSpace5, parkingSpace6, parkingSpace7, parkingSpace8));
        when(parkingSpaceRepository.findById(any())).thenReturn(Optional.of(parkingSpace3));

        //then
        Assertions.assertEquals(BigInteger.valueOf(13L), parkingService.randomParking());
    }

    @Test
    public void should_throw_error_when_random_parking_space_empty() {
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());
        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.randomParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }
}
