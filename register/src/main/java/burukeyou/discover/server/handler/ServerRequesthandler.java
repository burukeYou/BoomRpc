package burukeyou.discover.server.handler;

import burukeyou.common.protocol.RpcRequest;
import burukeyou.common.protocol.RpcResponse;
import burukeyou.discover.boot.ServerRegisterBoot;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ServerRequesthandler extends SimpleChannelInboundHandler<RpcRequest> {

    protected ServerRequesthandler(){}

    public static final ServerRequesthandler INSTACNCE = new ServerRequesthandler();

    private static final ThreadPoolExecutor requestThreadPool = new ThreadPoolExecutor(16,32,500L,
            TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(50000),new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        requestThreadPool.submit(() -> {
            System.out.println(msg);

           /* try {
                Thread.sleep(30000); // 模拟请求超时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(msg.getRequestId());

            try {
                Object res = handleRequest(msg);
                rpcResponse.setResult(res);
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setException(e);
            }
            System.out.println(rpcResponse);
            ctx.pipeline().writeAndFlush(rpcResponse);
        });
    }

    private Object handleRequest(RpcRequest request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String className = request.getClassName();
        className = className.substring(className.lastIndexOf(".")+1);
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        Object obj = ServerRegisterBoot.impClassMap.get(className);
        Class<?> clazz = obj.getClass();
        Method method = clazz.getMethod(methodName, parameterTypes);
        return  method.invoke(obj, parameters);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channel_Active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channel_Inactive");
    }
}
