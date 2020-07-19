package burukeyou.rpc.loadBalance;

import java.util.List;

/**
 *  轮寻
 */
public class RoundRobinStrategy extends LoadBalanceStrategy {

    private volatile static Integer pos = 0;

    @Override
    public synchronized <T> T getServer(List<T> serverList){
            if (pos >= serverList.size())
                pos = 0;
            return serverList.get(pos++);
    }

}
