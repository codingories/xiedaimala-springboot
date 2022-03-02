package hello.service;

import hello.dao.BlogDao;
import hello.entity.Blog;
import hello.entity.BlogResult;
import hello.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {


    private BlogDao blogDao;
    private UserService userService;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService) {
        this.blogDao = blogDao;
        this.userService = userService;
    }

    public BlogResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            // service需要知道很多东西，比如总共有多少博客，分多少页
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);

            // 对每一个blog再进行一个user查询，权宜之计，不要在生产中这么用
            blogs.forEach(blog -> blog.setUser(userService.getUserById(blog.getUserId())));

            int count = blogDao.count(userId);

            // 3个结果  每一页大小是2  page = 2
            int pageCount = count / pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResult.newResults(blogs, count, page, pageCount);
        } catch (Exception e) {
            System.out.println(e);
            return BlogResult.failure("系统异常");
        }
    };
}
