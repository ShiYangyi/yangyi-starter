package com.example.yangyistarter.util;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final UserService userService;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isUsableToken(token)) {
            Claims body = parseToken(token);
            fillAuthentication(response, body);
        }
        chain.doFilter(request, response);
    }

    private void fillAuthentication(HttpServletResponse response, Claims body) throws IOException {
        long userId = Long.parseLong(body.get("userId").toString());
        Optional<User> user = userService.findUserById(userId);
        if (user.isPresent()) {
            PreAuthenticatedAuthenticationToken authenticationToken = new
                    PreAuthenticatedAuthenticationToken(user.get(), null, AuthorityUtils.commaSeparatedStringToAuthorityList(user.get().getRole()));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("can't find user %s", userId));
        }
    }

    private Claims parseToken(String token) {
        Pattern pattern = Pattern.compile(SecurityConstants.TOKEN_PREFIX);
        Matcher matcher = pattern.matcher(token);
        return Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .parseClaimsJws(matcher.replaceFirst(""))
                .getBody();
    }

    private boolean isUsableToken(String token) {
        return token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX);
    }
}
