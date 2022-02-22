package hello.service;

import hello.mapper.UserMapper;

import javax.inject.Inject;

public class UserService {

    // 声明了在springboot容器中，有一个bean是mapper，并且命令spring说
    // 我依赖它，请你把它注入进来，这个时候报红，应为没有办法创造出userMapper，进行自动装配
    private UserMapper userMapper;
    @Inject
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Integer id) {
        return userMapper.findUserById(id);
    }
}
