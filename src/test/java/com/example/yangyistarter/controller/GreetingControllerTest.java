package com.example.yangyistarter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@AutoConfigureMockMvc
@SpringBootTest
public class GreetingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_refuse_when_request_greeting_interface() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = get("/hello")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }
}*/

@AutoConfigureMockMvc
@SpringBootTest
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //由于所有人都不能访问接口，所以这个测试方法是没有访问到controller层，被WebSecurityConfig所拦截，
    // 写的这个方法也就不能提高controller层的测试覆盖率，如果有些人可以访问，就可以增加相应测试
    @Test
    public void should_refuse_when_request_greeting_interface() throws Exception {

        mockMvc.perform(get("/hello").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}

