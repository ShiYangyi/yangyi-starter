package com.example.yangyistarter.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    public void should_return_id_2_when_set_id_2() {

        User user = User.builder().name("小明").password("1").build();
        user.setId(2L);
        Assertions.assertEquals(2L, user.getId());
    }

    @Test
    public void should_return_name_syyy_when_set_name_syyy() {

        User user = User.builder().id((2222L)).password("11111111").build();
        user.setName("syyy");
        Assertions.assertEquals("syyy", user.getName());
    }

    @Test
    public void should_return_password_222222_when_set_password_222222() {

        User user = User.builder().id((2222L)).name("小明").build();
        user.setPassword("222222");
        Assertions.assertEquals("222222", user.getPassword());
    }

    @Test
    public void should_return_role_ROLE_USER_when_set_role_ROLE_USER() {

        User user = User.builder().id((2222L)).name("小明").password("222222").build();
        user.setRole("ROLE_USER");
        Assertions.assertEquals("ROLE_USER", user.getRole());
    }

    @Test
    public void should_return_true_when_call_isAccountNonExpired() {
        User user = User.builder().id((2222L)).name("小明").build();
        Assertions.assertTrue(user.isAccountNonExpired());

    }

    @Test
    public void should_return_true_when_call_isAccountNonLocked() {
        User user = User.builder().id((2222L)).name("小明").build();
        Assertions.assertTrue(user.isAccountNonLocked());

    }

    @Test
    public void should_return_true_when_call_isCredentialsNonExpired() {
        User user = User.builder().id((2222L)).name("小明").build();
        Assertions.assertTrue(user.isCredentialsNonExpired());

    }

    @Test
    public void should_return_true_when_call_isEnabled() {
        User user = User.builder().id((2222L)).name("小明").build();
        Assertions.assertTrue(user.isEnabled());

    }
}