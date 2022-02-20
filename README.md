# 自动化测试工程
### 一、简介
工程依赖 **TestNG** 框架。 
测试报告Allure。
支持 **dubbo**、**restful** 接口.

>**TestNG在线资料**：  
>1.官网： https://testng.org/doc/index.html

### 二、使用说明
#### 1.自定义监听器
如果需要自定义监听器，则在testng.xml中添加listeners配置。如下：
```
<listeners>
        <listener class-name="lj.study.test.MyIMethodInterceptor"/>
</listeners>
```
#### 2. Dubbo类接入：

以调用open-service-api服务为例：  
2.1 首先添加maven依赖：
```$xslt
        <dependency>
            <groupId>com.lj.demo</groupId>
            <artifactId>open-service-api</artifactId>
            <version>1.0.1-SNAPSHOT</version>
        </dependency>
```
2.2 然后需要在 test/resources/dubbo-remote-test.xml 中添加对应的引用，如下：
```$xslt
<dubbo:reference id="userService"  interface="com.lj.open.api.UserService" timeout="10000" retries="0" version="1.0"/>
<dubbo:reference id="appService"  interface="com.lj.open.api.AppService" timeout="10000" retries="0" version="1.0"/>
```
2.3 使用时引用方式如下:
```$xslt
    @Autowired
    private AppService appService;
```
>注意：此方式引入，idea会提示错误：_Could not autowire. No beans of 'AppService' type fund._   
不用管，程序可以正常运行。

#### 3.Restful接口测试：
可以使用已经封装了的HTTP接口：get、post、put、delete；     
返回值支持返回httpclient、json、html、string等。

**Code Sample**：
1. get：
```$xslt
String apiResponse = HttpUtils.get(url);
```

2. post：
```$xslt
String apiResponse = HttpUtils.post(url, data);
```

### 三、使用规范：
#### 3.1 用例管理规范
1. 所有测试用例放在 `src/test/java`下，基础包为 `cn.lj.qa`
2. 公共方法抽象放在 `src/main/java`下
3. 每个工程的测试用例单独放在同一个package下
4. 定义testng-XXX.xml来指定运行某个(或某几个)模块下的用例，定义env来指定运行环境（test、pro、staging），运行命令：  
   `mvn clean test -DxmlFile=testng-XXX.xml -Denv=test
   `
5. **testng.xml** 中配置所有包下的case

#### 3.2 用例编写规范
1. 所有用例要求可重复运行（及数据准备、清理等需要实现）
2. 所有用例尽可能减少相互依赖，及可以单独运行
3. 测试结果校验尽可能全面，如 dbCheck、redisCheck等
4. 每个用例都要加上用例描述，如：`@Test(description = "测试说明")`
5. 如果希望某一个用例只在特定环境下运行，例如测试环境，请在该测试类的上方添加监听器 @Listeners(MyIMethodInterceptor.class)，同时在
   具体的测试方法前加上@TestEnv注解，默认加上就只在测试环境才会运行，如果想在预发或者生产环境运行，可以分别添加@TestEnv(value="ENV.STAGING")、
   @TestEnv(value="ENV.PRO")，不加注解或者加上@TestEnv(value="ENV.ALL")会在所有环境下运行


### 四、H5自动化
本项目H5自动化基于webdriver（chromedriver）headless模式。
####注意事项：
1. 当前用的驱动是 chromedriver headless模式，在 `cn.lj.qa.util.WebDriverUtil`中写死了驱动地址为`/usr/local/bin/chromedriver`，所以本地调试或者Linux自动运行机器上要保持驱动路径一致。
2. 推荐使用 ``Selenium IDE`` 浏览器插件（chrome或者firefox）录制、定位
3. 优先使用 `By.cssSelector`， 其次优先为 ``By.xpath``,最后是其他定位器
4. 注意区分 chromedriver 的版本，如 Mac， Linux， Windows等不同环境版本，地址 http://npm.taobao.org/mirrors/chromedriver/
5. 本工程使用 `RemoteWebDriver` 启动远程浏览器服务：
    - 镜像为：`dockerhub.test.lj.info/cn.lj.yl/webdriver:v2`，
    - K2服务为：`lj-qa`空间下的`webdriver`，
    - 调用demo：``WebDriver driver = new RemoteWebDriver(new URL("http://webdriver.lj-qa.k2-test.lj.info/wd/hub"), DesiredCapabilities.chrome());``

>参考文档：
https://github.com/SeleniumHQ/selenium/wiki/Grid2

### 未完待续。。。
