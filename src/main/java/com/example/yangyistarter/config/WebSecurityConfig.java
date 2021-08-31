package com.example.yangyistarter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*对请求进行配置*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //跨域资源共享，CORS通信，浏览器自动完成，CORS通信与同源AJAX通信一样，浏览器发现AJAX请求跨源，自动附加头信息，
        //只要服务器实现了CORS接口，就可以跨源通信。对于简单请求，浏览器在头信息增加Origin字段，发出CORS请求。
        // 非简单请求指对服务器有特殊要求，如请求方法是PUT或DELETE，或Content-Type字段类型是application/json。
        // Spring Security防护CSRF的filter限定是POST/PUT/DELETE等，没有限定GET Method。
        // 要保护的对象是产生数据改变的服务，对于读取数据的服务，不需要 CSRF保护。GET不修改数据，所以框架没有拦截Get请求。
        // 银行转账请求改变账户，会遭到 CSRF 攻击，需要保护。查询余额不会改变数据，CSRF 攻击无法解析服务器返回的结果，无需保护。
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers(HttpMethod.GET, "/hello").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()//增加表单登陆
                .and().httpBasic();//增加Basic认证
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore,对HttpSecurity做补充，这里排除掉/users/register路径，该路径不加入到Spring Security中，也就是不会做安全校验
        web.ignoring().antMatchers("/users/register");
    }
}
