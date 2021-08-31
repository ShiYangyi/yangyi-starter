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
}