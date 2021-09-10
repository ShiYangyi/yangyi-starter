package com.example.yangyistarter.util;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Optional;

//该类有了这个注解后，那么使用该类的地方就不要用new对象，而是把该类的对象注入进去
//@WebFilter(urlPatterns ="/users/messages", filterName ="messagesFilter")
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    /*
    有两种方式：
    第一种：通过@Autowired注解，然后该类上需要添加@Component注解
    第二种：不需要Spring来管理，自己手动写构造器。
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    /*@Override
    *//*从http头的Authorization读取token，用Jwts包的方法校验token合法性*//*
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }*/

    //所有的请求都会进入到这个方法，当token为空时，可能访问的是login接口，也可能访问的是messages接口，如果是login接口，应该不拦截，
    // 如果是messages接口，应该抛出错误，现在是两个接口会进行一致的处理，怎么区别两种请求，分别进行不同的动作。
    // 这里当token为空时，先不用抛错，因为这里只用判断将token存储进去，单一职责，至于权限，就在对应的接口上使用@hasRole()和@hasAuthority()这两个注解来解决。
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // 从header中取出Authorization，即token
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            // 去除prefix后，对JWToken进行解析
            Claims body = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                    //下面语句，替换字符串的时候，只用替换字符串开头的Bearer ，如果是编码后的字符串中间部分的Bearer 是不需要替换的。
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getBody();
            String userId = String.valueOf(body.get("userId"));
            Optional<User> user = userService.findUserById(new BigInteger(userId));
            //user是Optional类，使用user.isPresent()，而不是用user.get()!=null,并不等同，
            // 因为user.get()能够执行这个方法就说明这个不为空是恒成立的，因为get()方法只要成功就是不为空，否则就会抛错
            if (user.isPresent()) {
                PreAuthenticatedAuthenticationToken authenticationToken = new
                        //下面存进去的第一个参数，存进去user.get()，不要存进去Optional类。
                        PreAuthenticatedAuthenticationToken(user.get(), null, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // 如果用户不存在则返回401错误,Status code (401) indicating that the request requires HTTP authentication
                //这里不会抛出异常，只会给前端返回can't find user的异常信息，所以写单元测试时就是写mock的Response对象有没有调用这个方法
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", userId));
            }
        }
        /*else {
            throw new UnauthorizedException(ResponseCode.INVALID_USER_INFO, String.format("missing authorization header"));
        }*/
        //当token为空时，直接调用下面这条语句，导致请求就在这里被放走了，如果有些请求的路径需要认证后才能访问，那么需要可以在filter中进行拦截处理，抛出错误。
        chain.doFilter(request, response);
    }

    /*private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            //parse the token.
            String user = Jwts.parser()
                    .setSigningKey("MyJwtSecret")
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                PreAuthenticatedAuthenticationToken authenticationToken = new
                        PreAuthenticatedAuthenticationToken(user, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        return null;
    }*/
}
