package com.example.yangyistarter.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class UserTest {

    @Test
    public void should_return_id_2_when_set_id_2() {

        User user = User.builder().name("小明").password("1").build();
        user.setId(BigInteger.valueOf(2L));
        Assertions.assertEquals(BigInteger.valueOf(2L), user.getId());
    }

    @Test
    public void should_return_name_syyy_when_set_name_syyy() {

        User user = User.builder().id((BigInteger.valueOf(2222L))).password("11111111").build();
        user.setName("syyy");
        Assertions.assertEquals("syyy", user.getName());
    }

    @Test
    public void should_return_password_222222_when_set_password_222222() {

        User user = User.builder().id((BigInteger.valueOf(2222L))).name("小明").build();
        user.setPassword("222222");
        Assertions.assertEquals("222222", user.getPassword());
    }
}