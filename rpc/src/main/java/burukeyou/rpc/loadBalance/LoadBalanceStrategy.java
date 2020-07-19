package burukeyou.rpc.loadBalance;

import java.util.List;

public abstract class LoadBalanceStrategy {
      public abstract <T> T getServer(List<T> list);
}
