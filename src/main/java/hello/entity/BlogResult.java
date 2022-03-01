package hello.entity;

import java.util.List;

public class BlogResult extends Result<List<Blog>> {
    private int total;
    private int page;
    private int totalPage;

    // 使用工厂方法， 好处1.有名字，清楚告诉你意图 2.进行逻辑 3.返回null，new 不行，一定是一个对象。
    public static BlogResult newResults(List<Blog> data, int total, int page, int totalPage) {
        return new BlogResult("ok", "获取成功", data, total, page, total);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult("fail", msg, null, 0, 0, 0);
    }

    private BlogResult(String status, String msg, List<Blog> data, int total, int page, int totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
