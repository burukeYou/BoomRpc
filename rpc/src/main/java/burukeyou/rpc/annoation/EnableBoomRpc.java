package burukeyou.rpc.annoation;

import burukeyou.rpc.boot.RpcBoot;
import burukeyou.rpc.boot.RpcInjection;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *     开启 BoomRpc 的服务调用功能
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcBoot.class, RpcInjection.class})
public @interface EnableBoomRpc {

    //String[] provider() default {"burukeyou.rpc"};

    String[] provider() default {};
}
