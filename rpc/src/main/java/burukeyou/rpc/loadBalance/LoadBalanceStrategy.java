package burukeyou.rpc.loadBalance;

import java.util.List;

public interface LoadBalanceStrategy {

      <T> T getServer(List<T> list);
}
