package hello.entity;


public class Result {
    String status;
    String msg;
    boolean isLogin;
    Object data;

    public Object getData() {
        return data;
    }

    public Result(String status, String msg, boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    public Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    // 你想要声明一个json对象，就要给它声明一个相应的getter方法

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }
}