package burukeyou.provider;

import java.util.List;

public interface ProductService {

    List<String> getAllProductByUserId(String id);

    void buyOne(Integer productId);
}
