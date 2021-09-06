package com.example.yangyistarter.controller;

import com.example.yangyistarter.dto.UserDTO;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
import com.example.yangyistarter.util.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserService userService;

    @PostMapping("/register")
    //@Validated
    public ResponseCode register(@RequestBody @Valid UserDTO userDTO) {
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return userService.register(User.builder().id(userDTO.getId()).name(userDTO.getName()).password(userDTO.getPassword()).build());
    }

    /*
    1、用户向服务器发送用户名和密码。
    2、服务器验证通过后，在当前对话（session）里面保存相关数据，比如用户角色、登录时间等。
    3、服务器向用户返回一个 session_id，写入用户的 Cookie。
    4、用户随后的每一次请求，都会通过 Cookie，将 session_id 传回服务器。
    5、服务器收到 session_id，找到前期保存的数据，由此得知用户的身份。（这种方法的缺点是：只适用于单机，不适用分布式）

    于是有了JWT
    JWT 的原理是，服务器认证以后，生成一个 JSON 对象，发回给用户。
    用户与服务端通信时，都要发回这个 JSON 对象。
    服务器只靠这个对象认定用户身份。为了防止用户篡改数据，服务器在生成这个对象的时候，会加上签名。服务器不保存 session 数据，更容易实现扩展。
    实际的 JWT是一个很长的字符串，中间用点（.）分隔成三个部分。
    三个部分：
    Header（头部）
    Header 部分是一个 JSON 对象
    {
        "alg": "HS256",//签名的算法,默认是HS256.
        "typ": "JWT"//token的类型，JWT 令牌统一写为JWT
    }
    使用 Base64URL 算法将 JSON 对象转成字符串。

    Payload（负载）
    JSON 对象，存放实际需要传递的数据。
    使用 Base64URL 算法将 JSON 对象转成字符串。

    Signature（签名）
    对前两部分Header、Payload的签名，防止数据篡改。

    算出签名以后，把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"（.）分隔，就可以返回给用户。形式：Header.Payload.Signature
    JWT 默认不加密，任何人可读，不要把秘密信息放在jwt。

    JWT是json web token,JWT使用HMAC算法或RSA的公私秘钥进行签名。
    第一步：客户端向服务端发送post请求，并且发送用户名和密码。
    第二步：服务端使用私钥创建jwt，
    第三步：服务端向客户端返回jwt。
    第四步：客户端再次向服务端发送请求时，会在Authorization Header上添加jwt。
    第五步：服务端检查jwt签名，从jwt中获得用户信息。
    第六步：服务端返回响应给客户端。
    一旦用户登陆后，后面的请求中都会包含jwt。在单点登录中使用了该技术。

    优点：
    1。通过url，或者post参数或者http header中发送，传输速度快。
    2。包含了用户需要的信息，避免多次查询数据库。
    3。token以json加密的方式保存在客户端，不需要在服务端保存会话信息。
    jwt包含三部分：header，payload负载，signature签名。

    客户端收到服务器返回的 JWT，可以储存在 Cookie 里面，也可以储存在 localStorage。
    客户端与服务器通信，都要带上JWT。可以放在 Cookie 里自动发送，但不能跨域，更好做法是放在 HTTP 请求的头信息Authorization字段里。形式：Authorization: Bearer <token>
    另一种做法是，跨域时，JWT放在 POST 请求的数据体里。

    JWT缺点：
    服务器不保存session状态，因此无法在使用过程中废止token，或更改 token权限。一旦 JWT 签发了，在到期前始终有效。

    身份认证Authentication，和授权Authorization*/
    /*@PostMapping("/login")
    //@RequestBody表示方法参数与请求体相关联。通过该注解起到自动验证的作用。
    public ResponseCode login(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String encryptedPassword = user.getPassword();
        return userService.login(user, encryptedPassword);
    }*/

    @PostMapping("/login")
    //@Validated
    public Object login(@RequestBody @Valid UserDTO userDTO) {

        return userService.login(userDTO);
    }
}