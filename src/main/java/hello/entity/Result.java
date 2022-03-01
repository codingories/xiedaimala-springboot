package hello.entity;

// 编程abstract表示这个类不能直接实例化，只能使用它的子类
// 范型，可以理解成一个不确定的类型，T的类型编程具体的实在的类型
public abstract class Result<T> {
    String status;
    String msg;
    T data;

    public static Result failure(String s) {
        return LoginResult.failure(s);
    }


    public Object getData() {
        return data;
    }

    // protected表示当前构造器，只能在子类里面调用
    protected Result(String status, String msg) {
        this(status, msg, null);
    }

    public Result(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    // 你想要声明一个json对象，就要给它声明一个相应的getter方法

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}