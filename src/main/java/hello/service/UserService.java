package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Map<String, String> userPasswords = new ConcurrentHashMap<>();


    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        save("gebilaowang", "gebilaowang");
    }

    // Springboot容器是多线程的，多数情况不会发生问题，极少数情况会出现问题


    // 1. 保存用户名和密码保存下来
    public void save(String username, String password) {
        userPasswords.put(username, bCryptPasswordEncoder.encode(password));
    }

    // 2. 通过用户名拿到密码
    public String getPassword(String username) {
        return userPasswords.get(username);
    }

    // 声明了在springboot容器中，有一个bean是mapper，并且命令spring说
    // 我依赖它，请你把它注入进来，这个时候报红，应为没有办法创造出userMapper，进行自动装配

    public User getUserById(Integer id) {
        return null;
    }

    public User getUserByUsername(String username) {
        return new User(1, username);
    }

    // 实现接口，我问你用户名是什么，你告诉我用户信息是什么，如果用户不存在就丢一个UsernameNotFoundException异常
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!userPasswords.containsKey(username)) {
            throw new UsernameNotFoundException(username + "不存在");
        }

        String encodedPassword = userPasswords.get(username);
        return new org.springframework.security.core.userdetails.User(username, encodedPassword, Collections.emptyList());
    }
}
