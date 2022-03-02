package hello.dao;

import hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// 这样写一个接口，SQL，mybatis就能做完从数据库取数，装配成对象这样一个过程
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    // 当你传进去username的时候，会替换掉上面sql中的username
    User findUserByUsername(@Param("username") String username);

    @Select("insert into user(username, encrypted_password, created_at, updated_at) " +
            "values(#{username}, #{encryptedPassword}, now(), now())")
    void save(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Integer id);
}
