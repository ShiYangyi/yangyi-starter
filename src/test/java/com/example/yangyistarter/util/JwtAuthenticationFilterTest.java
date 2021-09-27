package com.example.yangyistarter.util;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class JwtAuthenticationFilterTest {

    @Test
    public void should_return_error_when_user_not_exist() throws ServletException, IOException {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserService userService = mock(UserService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, userService);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer "+
                //userId为18的token。
                Jwts.builder()
                        //把userId存储到了claim，这样解码时就是通过从claim获取userId来判断用户。编码时把用户信息存储到了哪里，判断用户解码时就是从哪个位置获取相应信息。
                        //也可以通过setSubject()把用户信息存储到subject，也可以通过其他的set()方法把用户信息存储到其他位置。
                        .claim("userId", 18L)
                        .setExpiration(Date.from(Instant.now().plus(SecurityConstants.EXPIRES, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                        .compact());
        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        when(userService.findUserById(18L)).thenReturn(Optional.empty());

        //判断mock对象有没有调用该方法
        verify(httpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", 18));
    }

    @Test
    public void should_return_success_when_user_exist() throws ServletException, IOException {
        /*这里mock了太多对象，应该把被测试方法拆分成多个子方法，mock对象为入参，这样比较好写测试方法。这里不用拆分，
        因为拆分后的子方法都是private修饰的，从外部访问不到，所以拆分后对于写测试没有什么实质性改进*/
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserService userService = mock(UserService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, userService);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        //User user = mock(User.class);
        //when(user.getId()).thenReturn(BigInteger.valueOf(18));
        User user = User.builder().id(18L).name("11112222").password("11111111111").build();
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer "+
                //userId为18的token。
                Jwts.builder()
                        //把userId存储到了claim，这样解码时就是通过从claim获取userId来判断用户。编码时把用户信息存储到了哪里，判断用户解码时就是从哪个位置获取相应信息。
                        //也可以通过setSubject()把用户信息存储到subject，也可以通过其他的set()方法把用户信息存储到其他位置。
                        .claim("userId", 18L)
                        .setExpiration(Date.from(Instant.now().plus(SecurityConstants.EXPIRES, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                        .compact());
        when(userService.findUserById(18L)).thenReturn(Optional.of(user));
        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //mock对象没有调用该方法
        verify(httpServletResponse, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", 18));
    }
}