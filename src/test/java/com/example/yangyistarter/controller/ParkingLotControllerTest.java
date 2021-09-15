package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.ParkingLot;
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

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class ParkingLotControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ParkingLot parkingLot = ParkingLot.builder().id(BigInteger.valueOf(1111L)).name("parking lot 1").username("yyyyy").build();
    String curJson = parkingLot.toString();

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void should_return_status_200_when_manager_add_parking_lot() throws Exception {

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(post("/parkinglot/add")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    //会自动加上ROLE_前缀
    @WithMockUser(roles = {"ASSISTANT"})
    public void should_return_status_403_when_assistant_add_parking_lot() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/parkinglot/add")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    //默认role是USER
    @WithMockUser()
    public void should_return_status_403_when_user_add_parking_lot() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/parkinglot/add")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
