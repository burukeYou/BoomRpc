package burukeyou.provider;

import burukeyou.discover.annoation.BoomService;

import java.util.Arrays;
import java.util.List;

@BoomService("ProductService")
public class ProductServiceImpl implements ProductService {

    @Override
    public List<String> getAllProductByUserId(String id) {
        return Arrays.asList("苹果","西瓜","饮料");
    }

    @Override
    public void buyOne(Integer productId) {
        System.out.println("购买商品:" + productId);
    }
}
