package burukeyou.client.rpc;

import burukeyou.rpc.annoation.BoomRpc;

import java.util.List;

@BoomRpc(name = "ServerA",callback = ProductServiceCallback.class)//
public interface ProductService {

    List<String> getAllProductByUserId(String id);

    void buyOne(Integer productId);
}
