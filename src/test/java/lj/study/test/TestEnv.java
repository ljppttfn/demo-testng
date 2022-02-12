package lj.study.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestEnv {
    //绝大多数情况下，加上这个注解我们希望该用例只在测试环境下执行，因此默认为TEST
    Env[] value() default Env.TEST;
}
