### 1. Dockerfile 构建镜像
docker build -t dockerhub.test..info/cn.ata.yl/webdriver:grid .

Dockerfile注意：
>当一个容器中运行多个进程时，最后一个必须以前台形式运行，前面的以后台形式运行。   
如果都已后台方式运行，则容器启动后直接退出。   
参考：https://blog.csdn.net/watcher0111/article/details/79929682


#### 2. 运行docker 容器
docker run --rm -ti -p 4444:4444 dockerhub.test..info/cn.ata.yl/webdriver:grid

    2.1 进入docker容器的2种方式：
   - docker exec -ti ${containerID} /bin/bash   推荐使用
   - docker attach ${containerID}





#### 3. 进入docker 容器，启动服务命令如下：
hub：
`java -jar selenium-server-standalone-3.141.59.jar -role hub -port 4444 -timeout 30 -browserTimeout 60 -maxSession 20`

node：
`java -jar selenium-server-standalone-3.141.59.jar -role node -hub http://127.0.0.1:4444/grid/register -maxSession 15 -browser browserName=chrome,maxInstances=10,platform=LINUX`


#### 4. 程序代码demo如下：
```
DesiredCapabilities capability = DesiredCapabilities.chrome();
        capability.setBrowserName("chrome");
        capability.setPlatform(Platform.LINUX);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--window-size=1290,1080", "--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-extensions");

//        capability.setCapability("chromeOptions", chromeOptions);
        capability.setCapability("goog:chromeOptions", chromeOptions);
        try {
//            driver = new RemoteWebDriver(new URL("http://webdriver.lj-qa.k2-test.ata.info/wd/hub"), capability);
            driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
            driver.get("http://www.baidu.com");
            System.out.println(driver.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            assert false;
        } finally {
//            driver.close();
            driver.quit();
        }
```