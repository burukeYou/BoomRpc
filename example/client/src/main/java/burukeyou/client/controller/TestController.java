package burukeyou.client.controller;


import burukeyou.client.rpc.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/b")
    public void testProductServiceBuyOne(){
        productService.buyOne(47289384);
    }

    @RequestMapping("/c")
    public List<String> testProductServiceGetAll(){
        return productService.getAllProductByUserId("5324534");
    }

}
