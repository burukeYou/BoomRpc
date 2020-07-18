package burukeyou.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;

/**
 * @author burukeyou
 * @date 2020-7-6
 */
@Component
@PropertySource(value = {"classpath:application.properties"},ignoreResourceNotFound = true)
public class BoomRpcProperties {

    /**
     *      服务名
     */
    @Value("${spring.application.name:null}")
    private String  applicationName;

    /**
     *  本服务的ip地址的端口
     */
    @Value("${server.port:8080}")
    private String serverPort;

    /**
     *   服务注册中心地址
     */
    @Value("${boomRpc.register.address:127.0.0.0.1:2181}")
    private String registerAddress;

    /**
     * 负载均衡策略配置
     *
     *     配置选项:
     *          roundRobin(轮询)
     *          random（随机）
     */
    @Value("${boomRpc.rpc.loadblacne:random}")
    private String loadblacne;


    //
    public String getLoadblacne() {
        return loadblacne;
    }

    public void setLoadblacne(String loadblacne) {
        this.loadblacne = loadblacne;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }
}
