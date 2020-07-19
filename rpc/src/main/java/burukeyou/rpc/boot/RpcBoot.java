package burukeyou.rpc.boot;

import burukeyou.common.util.ZkUtil;
import burukeyou.common.util.RpcCacheHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//
@Component
//@Configuration
@ComponentScan(basePackages = { "burukeyou.rpc", "burukeyou.common"})
public class RpcBoot implements ApplicationContextAware {

   // public static Map<Class<?>,String> serverMap = new HashMap<>();

    //public static Set<Class<?>> rpcServiceClazz;

    @Autowired
    private ZkUtil zkUtil;

    @Override
    public void setApplicationContext(ApplicationContext atx) throws BeansException {
        RpcCacheHolder.APPLICATION_CONTEXT = atx;

        // 缓存服务提供列表
        RpcCacheHolder.SUBSCRIBE_SERVICE.forEach(e -> { List<String> discover = zkUtil.discover(e);});

        // 观察订阅的服务列表
        AtomicInteger count = new AtomicInteger();
        new Thread(() -> {
            while (count.get() < 4){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count.getAndIncrement();
                if (RpcCacheHolder.SERVER_PROVIDERS.size() > 0 ){
                    System.out.println("=============服务提供者列表===========");
                    for (Map.Entry<String, List<String>> e : RpcCacheHolder.SERVER_PROVIDERS.entrySet()) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(e.getKey()+" ");
                        stringBuffer.append("【 ");
                        e.getValue().forEach(v -> stringBuffer.append(v+","));
                        stringBuffer.append(" 】");
                        System.out.println(stringBuffer.toString());
                    }
                    System.out.println("====================================");
                }
            }
        }).start();
    }



}
