package com.example.yangyistarter.config;

import com.example.yangyistarter.service.UserService;
import com.example.yangyistarter.util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//默认情况下所有请求都需要认证。
//启用Spring Security Web功能
@EnableWebSecurity
//声明为配置类
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//WebMvcConfigurerAdapter这个类是用来配置restful api的一些信息，序列化和反序列化等。
//WebSecurityConfigurerAdapter这个类才是用来配置spring security的一些信息。
//Spring Security为配置用户存储提供的方案：
//第一种：基于内存的用户存储。
//第二种：基于JDBC的用户存储，基于数据库后端。
//第三种：以LDAP作为后端的用户存储
//第四种：自定义用户详细服务
//上面四种方案都是通过覆盖WebSecurityConfigurerAdapter基础配置类中定义的configure()方法来配置。
//借助passwordEncoder()指定一个密码转换器encoder。passwordEncoder()方法能接收Spring Security中PasswordEncoder接口的任意实现。
// 例如BCryptPasswordEncoder使用bcrypt强哈希加密。
//更好的办法是使用spring data repository来存储用户。
//内部有UserDetail接口，通过这个接口可以提供更多信息给框架。如用户被授予哪些权限以及用户账号是否可用。
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    /*对请求进行配置
    * 接收一个HttpSecurity的对象，用来配置在web级别如何处理安全性。最常见需求是拦截请求以确保用户具有适当的权限。*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //跨域资源共享，CORS通信，浏览器自动完成，CORS通信与同源AJAX通信一样，浏览器发现AJAX请求跨源，自动附加头信息，
        //只要服务器实现了CORS接口，就可以跨源通信。对于简单请求，浏览器在头信息增加Origin字段，发出CORS请求。
        // 非简单请求指对服务器有特殊要求，如请求方法是PUT或DELETE，或Content-Type字段类型是application/json。
        // Spring Security防护CSRF的filter限定是POST/PUT/DELETE等，没有限定GET Method。
        // 要保护的对象是产生数据改变的服务，对于读取数据的服务，不需要 CSRF保护。GET不修改数据，所以框架没有拦截Get请求。
        // 银行转账请求改变账户，会遭到 CSRF 攻击，需要保护。查询余额不会改变数据，CSRF 攻击无法解析服务器返回的结果，无需保护。
        //下面的写法是按照顺序具有同样的优先级。
        http.cors()
                .and()
                //如果默认打开后，那么在后面的url上发送post请求时，就需要在post请求中带上凭证例如token才可以，如果不带上就会返回403forbidden。
                .csrf().disable()
                //对authorizeRequests()方法的调用会返回一个对象，基于该对象可以指定URL路径和这些路径的安全需求。
                .authorizeRequests()
                //表示这些请求允许所有用户访问，无条件允许访问
                //.antMatchers("/users/**").permitAll()
                //所有的请求都会进入到Filter类中执行
                .antMatchers("/users/login", "/users/register", "/parking").permitAll()
                //表示允许认证后的用户访问,只有写了这条语句后，后面写的Filter才可能生效。
                .anyRequest().authenticated()
                .and()
                //由于JwtAuthenticationFilter被@Component修饰，所以这里就不要用new对象
                //因为filetr是所有请求都会去访问的，如果不是要对所有请求都进行token的验证，那么就不应该把token是否存在的验证写在filter中，看filter和拦截器interceptor的区别。
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService))
                .sessionManagement()//定制session策略
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//让Spring Security不创建和使用session。

        //下面语句用来disable session，是和token不同的另一种认证模式，SessionCreationPolicy.STATELESS无状态
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //下面方法用来自定义表单登陆
                //.and().formLogin()//增加表单登陆
                //.and().httpBasic();//增加Basic认证
                /*.and()//这一条表示下面是其他的认证
                *//*上面是标准的Spring Security的处理流程，下面两行是将自定义的JWT方法加入到Spring Security的处理流程中*//*
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()));*/
    }

/*    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore,对HttpSecurity做补充，这里排除掉/users/register路径，该路径不加入到Spring Security中，也就是不会做安全校验
        //这个方法的底层代码涉及到firewall。
        web.ignoring().antMatchers("/users/register");
        web.ignoring().antMatchers("/users/login");
    }*/

    //对用户进行配置，通过AuthenticationManagerBuilder来配置认证过程中如何查找用户。
    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //简单地调用userDetailsService()，并且将自动装配到SecurityConfig中地UserDetailsService实例传递进去。配置一个密码转换器，数据库中存储得密码是转码后的。
    //首先需要声明一个PasswordEncoder类型的bean。然后再调用passwordEncode()方法将其注入到用户详情服务中。
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }*/

    /*@Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }*/

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
        super.addInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }*/

    /*@Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }*/
    /*
    存在多种方式来确定用户是谁。常用的四种方式：
    第一种：注入Principal对象到控制方法中
    第二种：注入Authentication对象到控制器方法中
    第三种：使用SecurityContextHolder来获取安全上下文。最麻烦的方法。这种方式可以在任何地方使用，其他三种方法都只能在控制器中使用。
    第四种：使用@AuthenticationPrincipal注解来标注方法，这是最常用的方式。这种方式好处在于：首先不需要进行类型转换，第二将安全相关的代码仅仅局限在注解本身。
     */

    /*如果注入出现问题，就需要自己手动写一个Bean，写一个构造器
    @Bean
    AuthenticationManager authenticationManager() {
    }*/
}
