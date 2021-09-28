package com.example.yangyistarter.controller;

import com.example.yangyistarter.dto.UserDTO;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
import com.example.yangyistarter.util.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    UserService userService = mock(UserService.class);
    BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    UserController userController = new UserController(bCryptPasswordEncoder, userService);
    UserDTO curUserDTO = mock(UserDTO.class);
    UserDTO userDTO = UserDTO.builder().id(1111L).name("poo").password("000000").role("ROLE_USER").build();
    User user = User.builder().id(1111L).name("poo").password("000000").build();

    @Test
    public void should_return_status_200_when_register() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/users/register")
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_200_when_login() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/users/login")
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser()
    public void should_return_status_200_when_get_message() throws Exception {
        MvcResult getMessagesResult = mockMvc.perform(get("/users/messages")
                        .with(user(user))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andReturn();
        assertThat(getMessagesResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void should_return_invalid_request_when_register_manager() {
        when(curUserDTO.getRole()).thenReturn("ROLE_MANAGER");
        Assertions.assertEquals(ResponseCode.ROLE_PERMISSION_DENY, userController.register(curUserDTO));
    }

    @Test
    public void should_return_invalid_request_when_register_clever_assistant() {
        when(curUserDTO.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        Assertions.assertEquals(ResponseCode.ROLE_PERMISSION_DENY, userController.register(curUserDTO));
    }

    @Test
    public void should_return_invalid_request_when_register_stupid_assistant() {
        when(curUserDTO.getRole()).thenReturn("ROLE_STUPID_ASSISTANT");
        Assertions.assertEquals(ResponseCode.ROLE_PERMISSION_DENY, userController.register(curUserDTO));
    }
}
