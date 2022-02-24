package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;

    // 这里需要mock服务，这个服务是真的，但是依赖是mock的并且注入进去
    @InjectMocks
    UserService userService = new UserService(mockEncoder, mockMapper);

    @Test
    public void testSave() {
        // 调用userService
        // 验证userService将请求转发给了userMapper

        // 要测试userService，调用userService传递两个参数，正确的行为是用户名和密码转交给userMapper

        // 当mockEncoder，encode "myPassowrd"的时候，希望它能返回"myEncodedPassword"
        // 1.为mockEncoder增加mock行为，2.去执行逻辑 3. 验证逻辑正常

        // given
        when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");

        // when:
        userService.save("myUser", "myPassword");

        // then:
        verify(mockMapper).save("myUser", "myEncodedPassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUsername("myUser");
        verify(mockMapper).findUserByUsername("myUser");
    }

    // loadUserByUsername拆成两个
    @Test
    public void throwExceptionWhenUserNotFound() {
//        Mockito.when(mockMapper.findUserByUsername("myUser")).thenReturn(null);
        // 保证() -> userService.loadUserByUsername("myUser")，一定会丢出异常，要不然就会失败
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("myUser"));

    }

    @Test
    public void returnUserDetailsWhenUserFound() {
        when(mockMapper.findUserByUsername("myUser"))
                .thenReturn(new User(123, "myUser", "myEncodedPassword"));
        UserDetails userDetails = userService.loadUserByUsername("myUser");
        // 第一个参数是预期得到的结果，第二个参数是实际得到的结果
        Assertions.assertEquals("myUser", userDetails.getUsername());
        Assertions.assertEquals("myEncodedPassword", userDetails.getPassword());

    }
}
