package burukeyou.rpc.degradation;

import burukeyou.common.protocol.RpcRequest;
import burukeyou.rpc.annoation.BoomRpc;

import java.lang.reflect.Method;

/**
 *      容错处理器
 */
public class Callbacker {

    private RpcRequest rpcRequest;

    private Class<?> callBackClass;
    private Class<?> callback;


    public static Callbacker Builder(RpcRequest rpcRequest){
        return new Callbacker(rpcRequest);
    }

    private  Callbacker(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
        try {
            System.out.println(rpcRequest);
            callBackClass =  Class.forName(rpcRequest.getClassName());
            callback = callBackClass.getAnnotation(BoomRpc.class).callback();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("1");
        }
    }


    public Callbacker IfNotCallback(Process process){
         if (!shouldCallback())
             process.doSomething();
        return this;
    }

    public Object orElseSet(Throwable throwable) throws Exception {
        if (shouldCallback()){
            Object obj = callback.newInstance();
            Method method = callback.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            Method setThrowable = callback.getMethod("setThrowable", Throwable.class);
            setThrowable.invoke(obj,throwable);
            return method.invoke(obj,rpcRequest.getParameters());
        }else
            return null;
    }

    private boolean shouldCallback(){
        return callback != void.class;
    }


    public interface Process {
        void doSomething();
    }
}
