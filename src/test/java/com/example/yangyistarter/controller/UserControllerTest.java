package com.example.yangyistarter.controller;

import com.example.yangyistarter.dto.UserDTO;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.util.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //User curUser = User.builder().name("po").password("0").build();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void should_return_status_200_when_register() throws Exception {

        //String curJson = "{\"name\": \"po00\", \"password\": \"000000\" }";
        UserDTO user = UserDTO.builder().id(1111L).name("poo").password("000000").role("ROLE_USER").build();
        String curJson = MAPPER.writeValueAsString(user);
        /*Request user = Request.builder().name("poo").password("000000").build();
        String curJson = user.toString();*/

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(post("/users/register")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_200_when_login() throws Exception {

        //这里不要用字符串来写，而要写一个请求类
        //Request{name='syy', password='1'}
        //String curJson = "{\"name\": \"po\", \"password\": \"0\" }";
        //Request user = Request.builder().name("poo").password("000000").build();
        //测试这里应该不需要是数据库中真实的数据对吧，不需要
        //之前跑测试在接口打断点，不能进入的原因是返回了400，返回了400是不会进入接口的，直接就被拦截了。最后在debug的日志中发现了是字符串解析时出了问题，
        // 重写的toString()方法漏写了一个双引号，由于下面.content()中参数应该传入json,json是一种字符串，但不是所有的字符串都是json，
        // 这里最好使用类转json的工具，这里用了writeValueAsString(),使用了ObjectMapper类构造了final static的对象。
        UserDTO user = UserDTO.builder().id(1111L).name("poo").password("000000").build();
        String curJson = MAPPER.writeValueAsString(user);

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(post("/users/login")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        //不管登陆成功还是登陆不成功，返回状态都是200，只是返回的信息不一致。
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    //添加下面的注解后，在运行方法时，会抛错，因为用了@WithMockUser注解后，方法中用到的是底层的userdetails.User，不是自己自定义的User类，所以会抛错。
    // 下面的注解使得：The following test will be run as a user with the username "user", the password "password", and the roles "ROLE_USER".
    //@WithMockUser
    @WithMockUser()
    public void should_return_status_200_when_get_message() throws Exception {

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        //MvcResult mvcResult = mockMvc.perform()
        //assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        /*
        perform()：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理.
        get:声明发送一个get请求的方法。MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables)：
        根据uri模板和uri变量值得到一个GET请求方式的。另外提供了其他的请求的方法，如：post、put、delete等。
        param：添加request的参数，如上面发送请求带上Authorization =   的参数。
        假如需要发送json数据格式,不能使用这种方式，改用@ResponseBody注解参数。
        andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确。
        andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台。
        andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理。
         */

        /*String responseString = mockMvc.perform(get("/users/messages")
                        //数据的格式
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        //添加参数
                        .param("Authorization","")
                        //返回的状态是200
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();   //将相应数据转换为字符串
        System.out.println("--------返回的json = " + responseString);*/

        /*
        given: login -> token
        when token -> call messages
        then successfully
        */

        /*UserDTO user = UserDTO.builder().id(BigInteger.valueOf(1111L)).name("poo").password("000000").build();
        String curJson = user.toString();

        //验证controller监听HTTP请求,调用MockMvc的perform()并提供要测试的URL
        MvcResult mvcResult = mockMvc.perform(post("/users/login")
                        //.content(objectMapper.writeValueAsString(curUser))
                        .content(curJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String loginResponse = mvcResult.getResponse().getContentAsString();*/
/*
仿造：
mvc
    .perform(get("/api/books/1")
    .with(user(customUser)));//可以看出user()是个方法，写出来后是import的一个静态方法。
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
然后里面的参数customer，由于user()方法只能传入两种参数：一个是String类型的对象，一个是UserDetails类的对象。
这里由于要的是User，所以这里应该传参为User类，为了两边统一，所以要让自定义User类继承UserDetails类。
 */
        User user = User.builder().id(1111L).name("poo").password("000000").build();
        MvcResult getMessagesResult = mockMvc.perform(get("/users/messages")
                                .with(user(user))
                                //数据的格式
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        //添加Authorization信息即token，要添加在header。
                        //.header("Authorization", "")
                )
                .andReturn();
        assertThat(getMessagesResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void should_return_invalid_request_when_register_manager() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getRole()).thenReturn("ROLE_MANAGER");
        UserController userController = new UserController();
        Assertions.assertEquals(ResponseCode.ROLE_PERMISSION_DENY, userController.register(userDTO));
    }

    @Test
    public void should_return_invalid_request_when_register_clever_assistant() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        UserController userController = new UserController();
        Assertions.assertEquals(ResponseCode.ROLE_PERMISSION_DENY, userController.register(userDTO));
    }

    @Test
    public void should_return_invalid_request_when_register_stupid_assistant() {
        UserDTO userDTO = mock(UserDTO.class);
        when(userDTO.getRole()).thenReturn("ROLE_STUPID_ASSISTANT");
        UserController userController = new UserController();
        Assertions.assertEquals(ResponseCode.ROLE_PERMISSION_DENY, userController.register(userDTO));
    }
}
