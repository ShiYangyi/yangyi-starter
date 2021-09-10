package com.example.yangyistarter.util;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
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
import java.math.BigInteger;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class JwtAuthenticationFilterTest {

    /*@Test
    public void should_return_success_when_user_exist() throws ServletException, IOException {
        *//*这里mock了太多对象，应该把被测试方法拆分成多个子方法，mock对象为入参，这样比较好写测试方法。这里不用拆分，
        因为拆分后的子方法都是private修饰的，从外部访问不到，所以拆分后对于写测试没有什么实质性改进*//*
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserService userService = mock(UserService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, userService);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password("10000000000").build();
        when(userService.findUserById(user.getId())).thenReturn(Optional.of(user));

        PreAuthenticatedAuthenticationToken authenticationToken = new
                PreAuthenticatedAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        Assertions.assertEquals(authenticationToken, SecurityContextHolder.getContext().getAuthentication());
    }*/

    @Test
    public void should_return_error_when_user_not_exist() throws ServletException, IOException {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserService userService = mock(UserService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, userService);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(
                //userId为18的token。
                "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiIxOCIsImV4cCI6MTYzMTk2MzIwNX0.ZwmDbraJ9FBzDmbS6qj9NWv6z_0genV1C95mkS-H_J3AMURiWQLFncUcBgwCjb-Og-9K0MWzzUZMxaB9u3hVBQ");
        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        when(userService.findUserById(BigInteger.valueOf(18L))).thenReturn(Optional.empty());

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
        User user = User.builder().id(new BigInteger(String.valueOf(18))).name("11112222").password("11111111111").build();
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(
                //userId为18的token。
                "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiIxOCIsImV4cCI6MTYzMTk2MzIwNX0.ZwmDbraJ9FBzDmbS6qj9NWv6z_0genV1C95mkS-H_J3AMURiWQLFncUcBgwCjb-Og-9K0MWzzUZMxaB9u3hVBQ");
        when(userService.findUserById(BigInteger.valueOf(18L))).thenReturn(Optional.of(user));
        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //mock对象没有调用该方法
        verify(httpServletResponse, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", 18));
    }
}