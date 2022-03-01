package hello.controller;

import hello.entity.LoginResult;
import hello.entity.Result;
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
    public Result auth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ? null: authentication.getName());

        if (loggedInUser == null) {
            return LoginResult.success( "用户没有登录", false);
        } else {
            return LoginResult.success( null, true, loggedInUser);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);
        if (loggedInUser == null) {
//            return new Result("fail", "用户没有登录", false);
            return LoginResult.failure("用户没有登录");
        } else {
            SecurityContextHolder.clearContext();
            return LoginResult.success("success", false);
        }
    }



    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
//            return new Result("fail", "username/password == null", false);
            return Result.failure("username/password == null");
        }
        if (username.length() < 1 || username.length() > 15) {
//            return new Result("fail", "invalid  username", false);
            return Result.failure("invalid  username");

        }
        if (password.length() < 1 || password.length() > 15) {
//            return new Result("fail", "invalid  password", false);
            return Result.failure("invalid  password");

        }

        // 验证逻辑不安全，并发情况下，假如说有两个用户同时的访问你的应用，在同时的用一个相同的username进行注册。

        User user = userService.getUserByUsername(username);
        if(user == null) {
            // 他们拿到的user，会同时执行这一句话
            userService.save(username, password);
            return LoginResult.success("ok", false);
        } else {
//            return new Result("fail", "user already exists", false);
            return LoginResult.success("user already exists", false);

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
//            return new Result("fail", "用户不存在", false);
            return LoginResult.failure("用户不存在");

        }
        // 把用户名和密码比对以下，看一下是不是要登录的那个人
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        // 管理鉴权的一个人
        // 3. 命令authenticationManager去拿真正的密码和它声称的密码进行比对
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
//            return new Result("ok", "登录成功", true, userService.getUserByUsername(username));
            return LoginResult.success("登录成功", true);
        } catch (BadCredentialsException e) {
//            return new Result("fail", "密码不正确", false);
            return LoginResult.failure("密码不正确");
        }
    }


}
