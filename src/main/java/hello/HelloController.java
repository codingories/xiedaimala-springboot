package hello;


import hello.service.User;
import hello.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;


@RestController
public class HelloController {

    private UserService userService;

    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public User index() {
        return this.userService.getUserById(1);
    }
}