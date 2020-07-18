package burukeyou.common.protocol;/*
package burukeyou.rpc.protocol;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultResponseFuture implements ResponseFuture{

    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseFuture.class);


    private static final Map<String, Channel> CHANNELS = new ConcurrentHashMap<>();

    private static final Map<String, DefaultResponseFuture> FUTURES = new ConcurrentHashMap<>();

    private final RpcRequest RpcRequest;

    private volatile RpcResponse rpcResponse;

    //
    public DefaultResponseFuture(burukeyou.rpc.protocol.RpcRequest rpcRequest) {
        RpcRequest = rpcRequest;
    }




    @Override
    public Object get() {
        return null;
    }

    @Override
    public Object get(int timeoutInMillis) {
        return null;
    }

    @Override
    public boolean isDone() {
        return RpcRequest != null;
    }
}
*/
