package burukeyou.discover.client;

import burukeyou.discover.annoation.EnableServerRegister;
import burukeyou.discover.config.BeanConfiguration;
import burukeyou.discover.entity.Preprocessing;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** 服务注册启动器
 *      user by not springboot project
 */
public class BoomRpcDiscoverApplication {

    public static AnnotationConfigApplicationContext context;

    public static Builder builder() {
        return new Builder();
    }

    public static void run(Class<?> primarySource) {
        builder().run(primarySource);
    }

    public static class Builder{

        public Builder() {
            if (context == null){
                synchronized (AnnotationConfigApplicationContext.class){
                    if (context == null){
                        context = new AnnotationConfigApplicationContext();
                    }
                }
            }
        }

        public Builder customize(Preprocessing preprocessing){
            preprocessing.doSomething(context);
            return this;
        }

        public  Builder register(Class<?>... annotatedClasses) {
             context.register(annotatedClasses);
             return this;
        }

        public void run(Class<?> primarySource){
            if (primarySource == null)
                throw new RuntimeException("BoomRpc discover application start fail , primarySource cant't not be null");

            EnableServerRegister annotation = primarySource.getAnnotation(EnableServerRegister.class);
            String[] strings = annotation.provider();

            if (strings.length > 0)
                context.scan(strings);

            context.register(BeanConfiguration.class);
            context.refresh();
        }
    }




}
