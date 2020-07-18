package burukeyou.client.rpc;

import burukeyou.rpc.degradation.Callback;

import java.util.Arrays;
import java.util.List;

public class ProductServiceCallback extends Callback implements ProductService {

    @Override
    public List<String> getAllProductByUserId(String id) {
        Throwable throwable = getThrowable();
        System.err.println("getAllProductByUserId服务调用失败： "+throwable.getMessage());
        return Arrays.asList("获取商品失败");
    }

    @Override
    public void buyOne(Integer productId) {
        Throwable throwable = getThrowable();
        System.err.println("buyOne服务调用失败： "+throwable.getMessage());
    }
}
