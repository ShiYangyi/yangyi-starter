package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

    ParkingSpace parkingSpace1 = ParkingSpace.builder().id(11L).receiptId(11L).isUsed(false).parkingLotName("parking_lot_1").build();
    ParkingSpace parkingSpace2 = ParkingSpace.builder().id(12L).receiptId(12L).isUsed(false).parkingLotName("parking_lot_1").build();
    ParkingSpace parkingSpace3 = ParkingSpace.builder().id(13L).receiptId(13L).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace4 = ParkingSpace.builder().id(14L).receiptId(14L).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace5 = ParkingSpace.builder().id(15L).receiptId(15L).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace6 = ParkingSpace.builder().id(16L).receiptId(16L).isUsed(false).parkingLotName("parking_lot_2").build();
    ParkingSpace parkingSpace7 = ParkingSpace.builder().id(17L).receiptId(17L).isUsed(true).parkingLotName("parking_lot_1").build();
    ParkingSpace parkingSpace8 = ParkingSpace.builder().id(18L).receiptId(18L).isUsed(true).parkingLotName("parking_lot_2").build();

    ParkingLot parkingLot1 = ParkingLot.builder().id(111L).name("parking_lot_1").username("user1").build();
    ParkingLot parkingLot2 = ParkingLot.builder().id(112L).name("parking_lot_2").username("user2").build();

    User user = mock(User.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);

    @Test
    public void should_return_available_receipt_id_when_available_parking_space_exist_and_user_not_log_in() {
        //when
        when(user.getRole()).thenReturn("ROLE_STUPID_ASSISTANT");
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace7, parkingSpace7));
        when(parkingSpaceRepository.findById(11L)).thenReturn(Optional.of(parkingSpace1));
        //mock的securityContextHolder无法调用getContext(),只有静态类才能调用
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        /*when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(null);*/
        //then
        Assertions.assertEquals(11L, parkingService.parking(user));
    }

    @Test
    public void should_throw_error_when_unavailable_parking_space_and_user_not_log_in() {
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());
        when(authentication.getPrincipal()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);
        //then
        //下面方法的使用，第一个参数是异常类，所以写.class，第二个参数写可执行的方法，所以写成下面这种表达式的形式。并且这条验证语句的返回值中就可以获取到抛出的异常信息
        /*Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());
        IllegalArgumentException err = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());*/
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking(user));
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_return_available_receipt_id_when_available_parking_space_exist_and_user_log_in() {
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace7, parkingSpace7));
        when(parkingSpaceRepository.findById(11L)).thenReturn(Optional.of(parkingSpace1));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_USER");
        //then
        Assertions.assertEquals(11L, parkingService.parking(user));
    }

    @Test
    public void should_throw_error_when_unavailable_parking_space_and_user_log_in() {
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_USER");
        //then
        //下面方法的使用，第一个参数是异常类，所以写.class，第二个参数写可执行的方法，所以写成下面这种表达式的形式。并且这条验证语句的返回值中就可以获取到抛出的异常信息
        /*Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());
        IllegalArgumentException err = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking());*/
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.parking(user));
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_return_available_receipt_id_when_clever_and_available_parking_space_exist_and_user_log_in() {
        //when
        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace4, parkingSpace5, parkingSpace6, parkingSpace7, parkingSpace8));
        when(parkingSpaceRepository.findById(13L)).thenReturn(Optional.of(parkingSpace3));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        //因为SecurityContextHolder是全局的，所以是不能mock对象的，于是需要把我这里mock的对象与SecurityContextHolder关联起来，通过set方法
        SecurityContextHolder.setContext(securityContext);
        //then
        Assertions.assertEquals(13L, parkingService.parking(user));
    }

    @Test
    public void should_throw_error_when_clever_parking_lot_empty_and_user_log_in() {
        //when
        when(parkingLotRepository.findAll()).thenReturn(Collections.emptyList());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.cleverParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_throw_error_when_clever_parking_space_unavailable_and_user_log_in() {
        //when
        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.cleverParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_throw_error_when_clever_parking_space_not_exist_and_user_log_in() {
        //when
        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace4, parkingSpace5, parkingSpace6, parkingSpace7, parkingSpace8));
        when(parkingSpaceRepository.findById(13L)).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.cleverParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }

    @Test
    public void should_return_available_receipt_id_when_random_and_available_parking_space_exist_and_user_log_in() {
        //given
        //不需要对随机数的生成方法进行mock，因为对我的测试无影响，可以在findById()方法中传入参数为anyInteger()
        //ThreadLocalRandom threadLocalRandom = mock(ThreadLocalRandom.class);
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Arrays.asList(parkingSpace1, parkingSpace2, parkingSpace3, parkingSpace4, parkingSpace5, parkingSpace6, parkingSpace7, parkingSpace8));
        when(parkingSpaceRepository.findById(any())).thenReturn(Optional.of(parkingSpace3));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_MANAGER");
        //then
        Assertions.assertEquals(13L, parkingService.randomParking());
    }

    @Test
    public void should_throw_error_when_random_parking_space_empty_and_user_log_in() {
        //when
        when(parkingSpaceRepository.findAll()).thenReturn(Collections.emptyList());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(user.getRole()).thenReturn("ROLE_MANAGER");
        //then
        IllegalArgumentException error = Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.randomParking());
        Assertions.assertEquals("没有合适的停车位", error.getMessage());
    }
}
