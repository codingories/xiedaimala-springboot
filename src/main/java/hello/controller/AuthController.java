package hello.controller;

import hello.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    // 会自动监测到WebSecurityConfig
    private UserDetailsService userDetailsService;
    // 鉴权的服务
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth ")
    @ResponseBody
    public  Object auth() {
        return new Result("ok" , "用户没有登录",false);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {

        System.out.println(usernameAndPasswordJson);
        // 1.拿出来用户名密码
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");

        // 这个过程中，命令userDetailsService查找相应的用户名，然后Spring帮你比对密码对不对
        // 我们需要一个提供用户信息的服务userDetailsService，还需要一个鉴权的服务，AuthenticationManager
        // 2. 去数据库里面拿到用户的真正的密码
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // 当用户找不到的时候直接丢一个用户不存在的异常
            return new Result("fail", "用户不存在", false);
        }
        // 把用户名和密码比对以下，看一下是不是要登录的那个人
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        // 管理鉴权的一个人
        // 3. 命令authenticationManager去拿真正的密码和它声称的密码进行比对
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            User loggedInUser = new User(1, "张三");
            return new Result("ok", "登录成功", true, loggedInUser);
        } catch (BadCredentialsException e) {
            return new Result("fail", "密码不正确", false);
        }

    }



    private static class Result {

        String status;
        String msg;
        boolean isLogin;
        Object data;

        public boolean isLogin() {
            return isLogin;
        }

        public Object getData() {
            return data;
        }

        public Result(String status, String msg, boolean isLogin) {
            this(status, msg, isLogin, null);
        }

        public Result(String status, String msg, boolean isLogin, Object data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }

        // 你想要声明一个json对象，就要给它声明一个相应的getter方法
        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

    }
}
