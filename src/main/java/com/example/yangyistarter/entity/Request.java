package com.example.yangyistarter.entity;

public class Request {
    private String name;
    private String password;

    Request(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    @Override
    public String toString() {
        //Request{name='syy', password='1'},这是原来toString()的格式
        //{"name": "po", "password": "0"}，重写后的格式
        String result = "{\"name\": \"" + name + '\"' + ", \"password\": \"" + password + '\"' + '}';
        return result;
    }

    public static class RequestBuilder {
        private String name;
        private String password;

        RequestBuilder() {
        }

        public RequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public Request build() {
            return new Request(name, password);
        }

        /*public String toString() {
            return "Request.RequestBuilder(name=" + this.name + ", password=" + this.password + ")";
        }*/
    }
}
