package com.example.yangyistarter.entity;

import lombok.Builder;

@Builder
public class Request {
    private String name;
    private String password;

    @Override
    public String toString() {
        //Request{name='syy', password='1'},这是原来toString()的格式
        //{"name": "po", "password": "0" }，重写后的格式
        return "{\"name\": \""+ name + '\"' +
                ", \"password\": \"" + password + '\"' +
                '}';
    }
}
