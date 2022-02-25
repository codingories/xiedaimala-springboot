package hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    // 测试基本的工具，假的mvc
    private MockMvc mvc;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    // 每个测试执行前都会执行。
    @BeforeEach
    void setUp() {
        // 通过这句话，我们告诉spring说我们要测new AuthController一个Controller, 其它都不关心
        // 明显这个也是要依赖两个mock的服务
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(
                result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录"))
        );
    }

    @Test
    void testLogin() throws Exception {
        /*
        未登录时，/auth接口返回未登录状态
        */
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(
                result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录"))
        );

        // 使用/auth/login登录
        Map<String, String> usernamePassword = new HashMap<>();
        usernamePassword.put("username", "MyUser");
        usernamePassword.put("password", "MyPassword");

        System.out.println(new ObjectMapper().writeValueAsString(usernamePassword));


        User newUser = new User("MyUser", bCryptPasswordEncoder.encode("myPassword"), Collections.emptyList());
        hello.entity.User myUser = new hello.entity.User(123, "MyUser", bCryptPasswordEncoder.encode("myPassword"));


        // mock一个userService，解决空指针问题
        when(userService.loadUserByUsername("MyUser"))
                .thenReturn(newUser);

        when(userService.getUserByUsername("MyUser"))
                .thenReturn(myUser);

        MvcResult response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(usernamePassword)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录成功")))
                .andReturn();



        // 单元测试，只是controller薄薄的一层，鉴权setCookie不在范围内
        // 简单理解，session是一组有状态的http请求和相应的集合
        HttpSession session = response.getRequest().getSession();

        System.out.println(session);
        // 再用拿到session执行一次登录操作
        // 再次检查/auth的返回值，处于登录状态
        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk()).andExpect(
                result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("MyUser"));
                }
        );
    }
}