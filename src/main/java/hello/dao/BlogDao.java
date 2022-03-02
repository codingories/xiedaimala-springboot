package hello.dao;

import hello.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        // 传递过去一个对象，使得mybatis能从参数对象中拿到参数，可以新建一个类做，最简单的方法就是用一个map
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", userId);
        parameters.put("offset", (page - 1) * pageSize);
        parameters.put("limit", pageSize);

        return sqlSession.selectList("selectBlog", parameters);
    };

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlog", userId);
    }
}
