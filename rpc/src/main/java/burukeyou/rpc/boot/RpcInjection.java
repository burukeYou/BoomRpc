package burukeyou.rpc.boot;

import burukeyou.common.util.RpcCacheHolder;
import burukeyou.rpc.annoation.BoomRpc;
import burukeyou.rpc.annoation.EnableBoomRpc;
import burukeyou.rpc.proxy.BoomRpcInvoker;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

public class RpcInjection implements ImportBeanDefinitionRegistrar {

    /**
     * AnnotationMetadata:当前类的注解信息；
     *          就是使用了@Import({RpcBoot.class})并且RpcBoot是ImportBeanDefinitionRegistrar的实现类才会被调用了
     *
     * BeanDefinitionRegistry：注册类，其registerBeanDefinition()可以注册bean
     **/
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableBoomRpc.class.getName());
        String[] provider = (String[])attributes.get("provider");

        Reflections reflections = new Reflections(provider);
        Set<Class<?>> rpcClazz = reflections.getTypesAnnotatedWith(BoomRpc.class, true);// honorInherited-不包含子类
        for (Class<?> e : rpcClazz) {
            BoomRpc annotation = e.getAnnotation(BoomRpc.class);
            String serverName = "".equals(annotation.name()) ? annotation.value() : annotation.name();
            BoomRpcInvoker boomRpcInvoker = new BoomRpcInvoker(serverName);
            Object proxyInstance = Proxy.newProxyInstance(e.getClassLoader(), new Class[]{e},boomRpcInvoker);
            SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry)registry;
            singletonBeanRegistry.registerSingleton(e.getName(),proxyInstance);
            //
            RpcCacheHolder.SUBSCRIBE_SERVICE.add(serverName);
        }
    }
}
