package burukeyou.rpc.loadBalance;

import java.util.List;
import java.util.Random;

/**
 *  随机
 */
public class RandomStrategy implements LoadBalanceStrategy {

    @Override
    public  <T> T getServer(List<T> list){
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

}
