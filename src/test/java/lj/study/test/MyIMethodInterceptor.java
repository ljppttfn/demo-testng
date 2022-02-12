package lj.study.test;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyIMethodInterceptor implements IMethodInterceptor {
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
        System.out.println("MyIMethodInterceptor");
        List<IMethodInstance> result = new ArrayList<>();
        for (IMethodInstance m : list) {
            TestEnv testEenv = m.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestEnv.class);
            if (null != testEenv && !Arrays.asList(testEenv.value()).contains(Env.ALL)) {
                //读取命令行传递的env参数
                String env_command = System.getProperty("env");

                //命令行传递的env参数：默认test
                env_command = env_command == null ? "test" : env_command;

                //获取测试方法的TestEnv注解值
                Env[] envs = testEenv.value();

                //判断测试方法中的注解是否包含命令行传参的env值
                for(Env env1:envs) {
                    if (env1.toString().equals(env_command.toUpperCase()))
                        result.add(m);
                }
            } else
                result.add(m);
        }

        return result;
    }
}
