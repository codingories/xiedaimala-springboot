package hello.mapper;

import hello.service.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// 这样写一个接口，SQL，mybatis就能做完从数据库取数，装配成对象这样一个过程
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(@Param("id") Integer id);
}
