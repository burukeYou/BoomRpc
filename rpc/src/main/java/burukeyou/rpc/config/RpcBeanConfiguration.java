package burukeyou.rpc.config;

import burukeyou.common.config.BoomRpcProperties;
import burukeyou.rpc.loadBalance.LoadBalanceContext;
import burukeyou.rpc.loadBalance.RandomStrategy;
import burukeyou.rpc.loadBalance.RoundRobinStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcBeanConfiguration {

    private final BoomRpcProperties properties;

    public RpcBeanConfiguration(BoomRpcProperties properties) {
        this.properties = properties;
    }

    @Bean
    public LoadBalanceContext loadBalanceContext(){
        LoadBalanceContext loadBalanceContext = new LoadBalanceContext();
        if ("roundRobin".equalsIgnoreCase(properties.getLoadblacne().trim())){
            loadBalanceContext.setLoadBalanceStrategy(new RoundRobinStrategy());
        }else if("random".equalsIgnoreCase(properties.getLoadblacne().trim())){
            loadBalanceContext.setLoadBalanceStrategy(new RandomStrategy());
        }else if("hash".equalsIgnoreCase(properties.getLoadblacne().trim())){
            // todo
        }else {
            loadBalanceContext.setLoadBalanceStrategy(new RandomStrategy());
        }
        return loadBalanceContext;
    }
}
