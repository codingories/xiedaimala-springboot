package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ? null: authentication.getName());

        if (loggedInUser == null) {
            return new Result("ok", "用户没有登录", false);
        } else {
            return new Result("ok", null, true, loggedInUser);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);
        if (loggedInUser == null) {
            return new Result("fail", "用户没有登录", false);
        } else {
            SecurityContextHolder.clearContext();
            return new Result("ok", "注销成功", false);
        }
    }



    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return new Result("fail", "username/password == null", false);
        }
        if (username.length() < 1 || username.length() > 15) {
            return new Result("fail", "invalid  username", false);
        }
        if (password.length() < 1 || password.length() > 15) {
            return new Result("fail", "invalid  password", false);
        }

        // 验证逻辑不安全，并发情况下，假如说有两个用户同时的访问你的应用，在同时的用一个相同的username进行注册。

        User user = userService.getUserByUsername(username);
        if(user == null) {
            // 他们拿到的user，会同时执行这一句话
            userService.save(username, password);
            return new Result("ok", "success!", false);
        } else {
            return new Result("fail", "user already exists", false);
        }

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
            userDetails = userService.loadUserByUsername(username);
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
            return new Result("ok", "登录成功", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return new Result("fail", "密码不正确", false);
        }
    }

    private static class Result {
        String status;
        String msg;
        boolean isLogin;
        Object data;

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

        public boolean isLogin() {
            return isLogin;
        }
    }
}
