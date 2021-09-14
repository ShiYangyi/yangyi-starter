package com.example.yangyistarter.util;

public class SecurityConstants {
    //如果不写这样一个私有的构造器，默认是有一个public的构造器，这样就需要新建一个该类的对象去写测试，
    // 如果该类中全是一些静态的方法，那么就可以加一个私有的构造器，测试覆盖率是不会去计算这些私有构造器的类的，而且在跑jacoco的时候，出现红色覆盖的是类名区域，也就是说需要对该类new实例对象去写测试。
    private SecurityConstants() {

    }
    public static final String SECRET = "MyJwtSecret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int EXPIRES = 100;
}