package hello.service;

import hello.dao.BlogDao;
import hello.entity.Blog;
import hello.entity.BlogResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {


    private BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            // service需要知道很多东西，比如总共有多少博客，分多少页
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);

            int count = blogDao.count(userId);

            // 3个结果  每一页大小是2  page = 2
            int pageCount = count / pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResult.newResults(blogs, count, page, pageCount);
        } catch (Exception e) {
            return BlogResult.failure("系统异常");
        }
    };
}
