package burukeyou.rpc.proxy;

import burukeyou.common.protocol.RpcRequest;
import burukeyou.common.protocol.RpcResponse;
import burukeyou.rpc.client.RpcClient;
import burukeyou.common.util.RpcCacheHolder;
import burukeyou.rpc.degradation.Callbacker;
import burukeyou.rpc.loadBalance.LoadBalanceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class BoomRpcInvoker implements InvocationHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String serverName;

    public BoomRpcInvoker(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        String className = method.getDeclaringClass().getName();
        rpcRequest.setClassName(className);
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);

        // 获得服务提供者列表
        List<String> providerList = RpcCacheHolder.SERVER_PROVIDERS.get(serverName);
        if (providerList == null || providerList.size() < 1 ){
            return Callbacker.Builder(rpcRequest)
                             .IfNotCallback(() -> {throw new RuntimeException(serverName+"服务不存在，调用失败");})
                             .orElseSet(new RuntimeException(serverName+"服务不存在，调用失败"));
        }

        // 负载均衡
        LoadBalanceContext loadBalanceContext = RpcCacheHolder.APPLICATION_CONTEXT.getBean(LoadBalanceContext.class);
        String serverIp = loadBalanceContext.executeLoadBalance(providerList);
        System.out.println("负载均衡: 调用服务" + serverName +"的" +  serverIp + " 服务器节点");

        //
        String[] host = serverIp.split(":");
        RpcClient rpcClient = new RpcClient(host[0].trim(), Integer.parseInt(host[1].trim()),serverName);

        RpcResponse rpcResponse = null;
        try {
            rpcResponse = rpcClient.sendRequest(rpcRequest);
            if (rpcResponse != null && rpcResponse.getException() != null){
                Exception e = rpcResponse.getException();
                return Callbacker.Builder(rpcRequest).IfNotCallback(e::printStackTrace).orElseSet(e);
            }
        } catch (Exception e) {
            // 捕获异常，容错处理
            return Callbacker.Builder(rpcRequest).IfNotCallback(e::printStackTrace).orElseSet(e);
        }

        return rpcResponse != null ? rpcResponse.getResult() : null;
    }
}
