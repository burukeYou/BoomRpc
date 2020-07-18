package burukeyou.discover.annoation;

import burukeyou.discover.boot.ServerRegisterBoot;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ServerRegisterBoot.class})
public @interface EnableServerRegister {
    /**
     *  Configure the package  location of the provider service
     *      <p>Only need configuration in case of No-SpringBoot Application</p>
     *      <p>In fact,  Configure  the location of the package scan </p>
     * @return
     */
    String[] provider() default {};
}
