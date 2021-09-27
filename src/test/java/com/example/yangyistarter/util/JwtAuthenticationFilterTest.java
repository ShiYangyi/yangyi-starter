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
                Jwts.builder()
                        .claim("userId", 18L)
                        .setExpiration(Date.from(Instant.now().plus(SecurityConstants.EXPIRES, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                        .compact());
        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        when(userService.findUserById(18L)).thenReturn(Optional.empty());
        verify(httpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", 18));
    }

    @Test
    public void should_return_success_when_user_exist() throws ServletException, IOException {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        UserService userService = mock(UserService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager, userService);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        User user = User.builder().id(18L).name("11112222").password("11111111111").build();
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer "+
                Jwts.builder()
                        .claim("userId", 18L)
                        .setExpiration(Date.from(Instant.now().plus(SecurityConstants.EXPIRES, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                        .compact());
        when(userService.findUserById(18L)).thenReturn(Optional.of(user));
        filter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        verify(httpServletResponse, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", 18));
    }
}