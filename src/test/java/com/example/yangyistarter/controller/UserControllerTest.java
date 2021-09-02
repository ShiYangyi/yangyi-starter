package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //User curUser = User.builder().name("po").password("0").build();

    @Test
    public void should_return_status_200_when_register() throws Exception {

        String curJson = "{\"name\": \"po\", \"password\": \"0\" }";

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(post("/users/register")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_200_when_login() throws Exception {

        //这里不要用字符串来写，而要写一个请求类
        //Request{name='syy', password='1'}
        //String curJson = "{\"name\": \"po\", \"password\": \"0\" }";
        Request user = Request.builder().name("po").password("0").build();
        String curJson = user.toString();

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(post("/users/login")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
