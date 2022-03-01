package hello.service;

import hello.dao.BlogDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    // 转交给数据库访问层
    @Mock
    BlogDao blogDao;
    @InjectMocks
    BlogService blogService;

    @Test
    public void getBlogsFromDb() {
         blogService.getBlogs(1, 10, null);
         // 我们调用getBlogs方法，我们希望请求被转交给了blogDao方法
         Mockito.verify(blogDao).getBlogs(1, 10, null);
    }

//    @Test
//    public void returnFailureWhenExceptionThrown() {
//        when(blogDao.getBlogs(anyInt(), anyInt(), any())).thenThrow(new Exception());
//        blogService.getBlogs(1,10,null);
//    }



}
