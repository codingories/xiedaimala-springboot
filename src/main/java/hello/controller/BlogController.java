package hello.controller;

import hello.entity.LoginResult;
import hello.entity.Result;
import hello.entity.User;
import hello.service.BlogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

// 告诉Spring boot说这是一个组件component，同时这是一个Controller
@Controller
public class BlogController {
    private BlogService blogService;

    // 同时注入进来服务
    @Inject
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public Result getBlogs(@RequestParam("page") Integer page, @RequestParam(value = "userId", required = false) Integer userId) {
        if (page == null || page < 0) {
            page = 1;
        }
        return blogService.getBlogs(page, 10, userId);
    }
}
