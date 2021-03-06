package com.example.yangyistarter.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDTOTest {

    @Test
    public void should_return_id_2_when_set_id_2() {

        UserDTO userDTO = UserDTO.builder().name("小明").password("1").build();
        userDTO.setId(2L);
        Assertions.assertEquals(2L, userDTO.getId());
    }

    @Test
    public void should_return_name_syyy_when_set_name_syyy() {

        UserDTO userDTO = UserDTO.builder().id((2222L)).password("11111111").build();
        userDTO.setName("syyy");
        Assertions.assertEquals("syyy", userDTO.getName());
    }

    @Test
    public void should_return_role_ROLE_MANAGER_when_set_role_ROLE_MANAGER() {

        UserDTO userDTO = UserDTO.builder().id((2222L)).name("syyy").password("11111111").build();
        userDTO.setRole("ROLE_MANAGER");
        Assertions.assertEquals("ROLE_MANAGER", userDTO.getRole());
    }

    @Test
    public void should_return_string_when_call_toString() {

        UserDTO.UserDTOBuilder userDTOBuilder = new UserDTO.UserDTOBuilder();
        userDTOBuilder.id((2222L));
        userDTOBuilder.name("小明");
        userDTOBuilder.password("1111111");
        userDTOBuilder.role("ROLE_USER");
        Assertions.assertEquals("UserDTO.UserDTOBuilder(id=2222, name=小明, password=1111111, role=ROLE_USER)", userDTOBuilder.toString());

    }
}
