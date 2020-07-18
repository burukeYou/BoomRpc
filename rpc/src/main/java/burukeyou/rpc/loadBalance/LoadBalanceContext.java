package burukeyou.rpc.loadBalance;


import java.util.List;

public class LoadBalanceContext {

    private LoadBalanceStrategy loadBalanceStrategy;

    public  <T> T executeLoadBalance(List<T> list){
         return loadBalanceStrategy.getServer(list);
    }

    public void setLoadBalanceStrategy(LoadBalanceStrategy loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }
}
