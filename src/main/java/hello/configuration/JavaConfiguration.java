package hello.configuration;

import hello.mapper.UserMapper;
import hello.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// 这个配置告诉Spring说，我想要一个userService，你应该给我一个新的userService
@Configuration
public class JavaConfiguration {
//    @Bean
//    public UserService userService(UserMapper userMapper) {
//        // 装配UserService的时候需要一个UserMapper，这个UserMapper从哪里来？
//        // 这就是Spring的好处了，当你需要一个UserMapper的时候直接放进去，就能够自动的获取，就是这么简单
//        return new UserService(userMapper);
//    }
}