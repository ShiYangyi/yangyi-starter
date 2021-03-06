package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class ManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    User user = User.builder().id(1113L).name("yyyY").password("yyyyyy").role("ROLE_USER").build();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void should_return_status_200_when_manager_add_assistant() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/stuffs")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    //会自动加上ROLE_前缀
    @WithMockUser(roles = {"CLEVER_ASSISTANT"})
    public void should_return_status_403_when_clever_assistant_add_assistant() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/stuffs")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    //会自动加上ROLE_前缀
    @WithMockUser(roles = {"STUPID_ASSISTANT"})
    public void should_return_status_403_when_stupid_assistant_add_assistant() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/stuffs")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    //默认role是USER
    @WithMockUser()
    public void should_return_status_403_when_user_add_assistant() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/stuffs")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    //会自动补齐前缀ROLE_
    @WithMockUser(roles = {"MANAGER"})
    public void should_return_status_200_when_manager_delete_clever_assistant() throws Exception {

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(delete("/stuffs/yyyY")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    //会自动加上ROLE_前缀
    @WithMockUser(roles = {"CLEVER_ASSISTANT"})
    public void should_return_status_403_when_clever_assistant_delete_clever_assistant() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/stuffs/yyyY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    //会自动加上ROLE_前缀
    @WithMockUser(roles = {"STUPID_ASSISTANT"})
    public void should_return_status_403_when_stupid_assistant_delete_clever_assistant() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/stuffs/yyyY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    //默认role是USER
    @WithMockUser()
    public void should_return_status_403_when_user_delete_clever_assistant() throws Exception {
        //从导入的包中可以看到源码里除了get()/post()/put()/patch()/delete()
        MvcResult mvcResult = mockMvc.perform(delete("/stuffs/yyyY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
