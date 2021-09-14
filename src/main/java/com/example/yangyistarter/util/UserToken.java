package com.example.yangyistarter.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

//每次都是new来创建实例对象，又不是全局唯一，所以不应该添加@Component注解，并且类里面的构造器方法也是多余的，每次使用该类时都是通过new对象。
//@Component
@AllArgsConstructor
public class UserToken {
    //@Getter
    private String userId;
    private String password;
    // int minutes
    private int expires;
    /*public UserToken(@Value("708") String userId,
                                    @Value("SecretKeyToGenJWTs") String password,
                                    //expires值为1，单位为分钟
                                    @Value("1") int expires) {
        this.userId = userId;
        this.password = password;
        this.expires = expires;
    }*/

    public String getToken() {
        //当发现解码后的token与本地生成的token不匹配时候，就应该把解码部分的代码与获取token的代码写在一起，
        // 这样看刚刚获取到的编码得到的token是否可以成功解码获取到用户信息，这样就是从最小步骤开始调试，避免在传输过程中可能导致的错误
        String token = Jwts.builder()
                //把userId存储到了claim，这样解码时就是通过从claim获取userId来判断用户。编码时把用户信息存储到了哪里，判断用户解码时就是从哪个位置获取相应信息。
                //也可以通过setSubject()把用户信息存储到subject，也可以通过其他的set()方法把用户信息存储到其他位置。
                .claim("userId", userId)
                .setExpiration(Date.from(Instant.now().plus(expires, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .compact();
        /*Claims body = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getBody();*/
        return token;
    }
}










