package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//控制层的测试方法是集成测试，所以虽然是对controller层方法进行测试，也是会运行到service层中的方法，集成测试不要mock对象。对service层的代码打断点，
// 看到repository是空的，这是因为测试中使用的数据库不是mysql，而是h2数据库，在配置文件中可以看到。
//所以有三种解决办法：第一种，写一个事务，在事务里执行添加数据，然后再回滚事务。
// 第二种：用Junit的@Before注解来初始化数据。
// 第三种：直接在测试代码中给repository中填充几条数据（最简单）
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class ParkingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ParkingSpaceRepository parkingSpaceRepository;
    @Autowired
    ParkingLotRepository parkingLotRepository;

    //这里自己设置的id值是不会起作用的，因为数据库id设置了自增，所以测试中数据的id会从1开始自增
    ParkingSpace parkingSpace = ParkingSpace.builder().id(BigInteger.valueOf(1L)).receiptId(BigInteger.valueOf(111L)).isUsed(false).parkingLotName("parking_lot_1").build();
    ParkingLot parkingLot = ParkingLot.builder().id(BigInteger.valueOf(1L)).name("parking_lot_1").username("user1").build();

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void should_return_status_200_when_manager_request_parking() throws Exception {
        parkingLotRepository.save(parkingLot);
        parkingSpaceRepository.save(parkingSpace);
        MvcResult mvcResult = mockMvc.perform(post("/parking/managers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(roles = {"CLEVER_ASSISTANT"})
    public void should_return_status_200_when_clever_assistant_request_parking() throws Exception {
        parkingLotRepository.save(parkingLot);
        parkingSpaceRepository.save(parkingSpace);
        MvcResult mvcResult = mockMvc.perform(post("/parking/clever/assistants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(roles = {"STUPID_ASSISTANT"})
    public void should_return_status_200_when_stupid_assistant_request_parking() throws Exception {
        parkingSpaceRepository.save(parkingSpace);
        MvcResult mvcResult = mockMvc.perform(post("/parking/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    public void should_return_status_200_when_user_request_parking() throws Exception {

        parkingSpaceRepository.save(parkingSpace);
        MvcResult mvcResult = mockMvc.perform(post("/parking/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
