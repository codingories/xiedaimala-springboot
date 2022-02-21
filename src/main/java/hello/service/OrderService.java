package hello.service;


import javax.inject.Inject;

// 第三种方式，构造器
public class OrderService {

    private UserService userService;

    @Inject
    public OrderService(UserService userService) {
        this.userService = userService;
    }

    public void placeOrder(Integer userId, String Item) {
        userService.getUserById(userId);
    }
}
