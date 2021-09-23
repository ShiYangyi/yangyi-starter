package com.example.yangyistarter.controller;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PickingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void should_return_status_200_when_manger_request_picking_up_car() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/cars/?receiptId=10")
                //出现400 bad request错误的原因是：content("111")这个里面应该传入json，不应该传入字符串，json是这样传的，参考postman，"{key:value}",但是传入content，
                        // 一般是对应在控制层方法入参处写的是@RequestBody，而现在这里写的是@RequestParam，所以参考postman，传入参数后url变为：localhost:8080/cars?receiptId=10，
                        // 所以这里不应该写content()，而是对上面post()中的url按照postman中一样操作。
                        //.content("111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    public void should_return_status_200_when_user_request_picking_up_car() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/cars/?receiptId=10")
                        //.content("111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(roles = {"STUPID_ASSISTANT"})
    public void should_return_status_200_when_user_stupid_assistant_request_picking_up_car() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/cars/?receiptId=10")
                        //.content("111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(roles = {"CLEVER_ASSISTANT"})
    public void should_return_status_200_when_user_clever_assistant_request_picking_up_car() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/cars/?receiptId=10")
                        //.content("111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
