package burukeyou.discover.boot;


import burukeyou.common.util.RpcCacheHolder;
import burukeyou.discover.annoation.BoomService;

import burukeyou.common.config.BoomRpcProperties;
import burukeyou.discover.server.RpcServer;
import burukeyou.common.util.ZkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Component
@Configuration
@ComponentScan(basePackages = { "burukeyou.discover", "burukeyou.common"})
public class ServerRegisterBoot implements ApplicationContextAware, InitializingBean {

    private Logger logger =  LoggerFactory.getLogger(getClass());

    @Autowired
    private BoomRpcProperties boomRpcProperties;

    @Autowired
    private ZkUtil zkUtil;

    @Autowired
    private RpcServer rpcServer;

    // 维护本地服务接口和其实现类的关系
    public static Map<String, Object> impClassMap = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext atx) throws BeansException {
        if (boomRpcProperties.getApplicationName() == null || boomRpcProperties.getApplicationName().equals(""))
           return;

        // 1
        Map<String, Object> serviceMap = atx.getBeansWithAnnotation(BoomService.class);
        for (Object e : serviceMap.values()) {
            impClassMap.put(e.getClass().getAnnotation(BoomService.class).value(),e);
        }

        //
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String address = zkUtil.register("/" + boomRpcProperties.getApplicationName() + "/" + ip + ":" + boomRpcProperties.getServerPort(), "");
            if (address != null && address != ""){
                logger.info("=========================================");
                logger.info("服务发布成功");
                logger.info("=========================================");
                rpcServer.start();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
