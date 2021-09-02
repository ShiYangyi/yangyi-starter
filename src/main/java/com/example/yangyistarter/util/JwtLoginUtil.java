package com.example.yangyistarter.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.yangyistarter.entity.User;

import java.util.Date;

//如果其他类中要@Autowired注入这个类，则这个类上是需要注解的，如@Controller/@Service/@Repository/@Bean，其他的@Component。否则是注入不成功的。
//但是由于这里是写的工具类，与其他的类没有依赖，所以就可以不用在类上写注解，而是把类中的方法用static修饰，
// 那么其他类中就可以直接用类去调用。注意，由于是工具类，不希望去new这个对象，
// 那么就把它的构造器用private去修饰，这样在测试覆盖率时就会跳过这个方法，如果不这样写，那么测试时还需要对这个构造器写测试方法。
public class JwtLoginUtil {

    private JwtLoginUtil() {
    }
    private static final long EXPIRE_TIME = 2 * 60 * 1000;//2*60毫秒，即2min

    public static String getToken(User user) {
        //在token中填充的是id信息而不是用户名，保证唯一性，withAudience()存入需要保存在token的信息，这里把用户ID存入token中
        //指定token中第二部分payload中的audience。构造token包括了这些方法：
        //withHeader()/
        //通过自带的withExpiresAt()，验证token，当前时间超过了过期时间，那么验证就不会通过，直接报错，提示用户去登录。
        String token= JWT.create().withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME)).withAudience(user.getId().toString())
                .sign(Algorithm.HMAC256(user.getPassword()));//把用户密码作为加密算法的私钥
        return token;
    }
}
