package lj.study.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class WebDriverUtil {


    /**
     * 返回默认手机模式的webdriver，其中 window-size=1280,1024
     *
     * @return ChromeDriver
     */
    public static WebDriver getH5Driver() {

        ChromeOptions chromeOptions = new ChromeOptions();
        //经发现，添加下面参数时，在headless模式下，click操作无效
        //设置成手机模式之后，使用click事件，进行搜索，无响应，我们可以使用TouchActions中tap方式去处理? //
//        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        chromeOptions.addArguments("--window-size=1280,1024");
        return getRemoteDriver(chromeOptions);
        // return getLocalDriver(chromeOptions);
    }


    /**
     * 返回本地的 ChromeDriver
     *
     * @param chromeOptions chrome参数
     * @return chrome driver
     */
    public static WebDriver getLocalDriver(ChromeOptions chromeOptions) {

        //设置chrome驱动地址
        //特别注意：本地mac无法在 /home下新建目录，而测试环境的Jenkins机器又只能在 /home/appweb 下有权限
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage", "--disable-extensions", "--hide-scrollbars");
        String os_name = System.getProperty("os.name");
        if (os_name.contains("Linux")) {
            // 方式一：用一个固定路径, Jenkins机器用此路径
            System.setProperty("webdriver.chrome.driver", "/home/appweb/webdriver/chromedriver");
        } else {
            // 方式二：用本项目文件夹中的驱动，即相对路径。
            // 特别注意：当前项目中的 chromedriver 为mac 版本（Linux、Windows版本要单独下载并设置）
            File file = new File("");
            String base_dir = file.getAbsolutePath();
            System.out.println(">>> getAbsolutePath: " + file.getAbsolutePath());
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\chromedriver.exe");
        }

        WebDriver driver = new ChromeDriver(chromeOptions);
        //设置元素等待超时时间（隐式等待）
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //设置页面加载超时时间
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        //设置脚本执行超时时间
        driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);

        return driver;
    }


    /**
     * 连接远程的 WebDriver grid 测试平台
     * @param chromeOptions  chrome参数
     * @return
     */
    public static WebDriver getRemoteDriver(ChromeOptions chromeOptions){
        //设置为 headless 模式启动，即后台运行，无需弹出chrome浏览器窗口
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage", "--disable-extensions", "--hide-scrollbars");

        //走远程调用模式 RemoteWebDriver，本地和jenkins机器都可以连接，无需各自做环境适配
        WebDriver driver = null;
        DesiredCapabilities capability = DesiredCapabilities.chrome();

        //应该只有设置 goog:chromeOptions 属性，才能在 node 加上 -browser 参数模式下运行成功，否则会报 unknown error: DevToolsActivePort file doesn't exist
        //但如果不加 -browser， 2者都可以使用。
//        capability.setCapability("chromeOptions", chromeOptions);
        capability.setCapability("goog:chromeOptions", chromeOptions);
        capability.setBrowserName("chrome");
        capability.setPlatform(Platform.LINUX);

        try {
            driver = new RemoteWebDriver(new URL("http://webdriver.lj-qa.k2-test.ata.info/wd/hub"), capability);
//            driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //设置元素等待超时时间（隐式等待）
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //设置页面加载超时时间
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        //设置脚本执行超时时间
        driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);

        return driver;
    }

    /**
     * 将当前页面截图存储
     *
     * @param driver       webdriver对象
     * @param filePathName 指定存储的文件路径+文件名，默认根目录是 screen/
     */
    public static void screenShoter(WebDriver driver, String filePathName) {
        System.out.println(">>> 开始截屏：");
        File file = new File("");
        System.out.println(">>> getAbsolutePath: " + file.getAbsolutePath());
        String baseDir = file.getAbsolutePath();
        File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenShotFile, new File(baseDir + "/screen/" + filePathName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断元素是否存在
     *
     * @param driver webdriver
     * @param by     By
     * @return boolean
     */
    public static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            System.out.println(">>> exception msg: " + e.getMessage());
            return false;
        }
    }

    /**
     * 查找元素
     * @param driver
     * @param loc
     * @param ele_type
     * @param wait_time
     */
    public static WebElement find_element(WebDriver driver, By loc, int ele_type , int wait_time){
        WebElement ele = null;
        try {
            if (ele_type == 1) {
                ele = new WebDriverWait(driver, wait_time).until(ExpectedConditions.presenceOfElementLocated(loc));
            } else if (ele_type == 2) {
                ele = new WebDriverWait(driver, wait_time).until(ExpectedConditions.visibilityOfElementLocated(loc));
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("没有定位到元素： " + e.getMessage());
        }
        return ele;
    }


    /**
     * 将当前页面向下拉，直至指定元素可见
     *
     * @param driver webdriver
     * @param by     指定可见元素
     */
    public static void scrollToElement(WebDriver driver, By by) {
        //页面向下滚动翻页计数器，每次下拉500px：scroll(0,500*i) ,
        //防止出现死循环，翻10次
        int i = 1;
        while (!isElementPresent(driver, by) && i<10) {
            ((JavascriptExecutor) driver).executeScript("scroll(0," + 500 * i + ")");
            i++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
