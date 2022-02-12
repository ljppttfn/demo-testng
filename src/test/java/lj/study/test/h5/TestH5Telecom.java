package lj.study.test.h5;

import lj.study.utils.WebDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TestH5Telecom {

    @Test
    private String test_h5_demo(String desc) {
        String url="";
        String task_id = null;
        WebDriver driver = WebDriverUtil.getH5Driver();
        driver.get(url);

        try {
            // 如果是微信公众号，则先点击主页上的图标，进入机构H5主页
            if (desc.equals("WX")) {
                //点击公众号主页上的 运营商 图标，注意：代码中命名出错了，用的 commerce 而非 telecom
                driver.findElement(By.cssSelector(".commerce-icon")).click();
            }

            //By xpath, 总是抛异常：org.openqa.selenium.ElementNotVisibleException: element not interactable
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("input.van-field__control")).sendKeys(String.valueOf(13218010000l + new Random().nextInt(1000)));
            driver.findElement(By.name("password")).sendKeys("123456");
            driver.findElement(By.cssSelector("button.submit-btn.theme-btn")).click();

            Thread.sleep(1000);

            //设置等待时间，一次交互，时间简单设置120s
            WebDriverWait wait = new WebDriverWait(driver, 120);

            //等待请求结果，至出现短信验证码弹框
            wait.until(presenceOfElementLocated(By.linkText("确认")));

            //*************************** 下面为短信验证码重试过程 start ***************************
            //输入错误短信验证码，并提交
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[3]/div[1]/div/div[5]/input ")).sendKeys("000000");
            driver.findElement(By.linkText("确认")).click();

            //爬虫进行中，直至出现短信验证码错误，再次输入弹框
            //用 By.linkText("发送验证码") 定位不到元素, 用下面的css可以定位：By.linkText 仅限用于 <a>元素
            wait.until(presenceOfElementLocated(By.linkText("确认")));

            //此次不输入验证码，等待60s后，手动重发验证码
//            Thread.sleep(65000);  //或者直接等待60s以上
            wait.until(presenceOfElementLocated(By.cssSelector("div.before-Send")));
            driver.findElement(By.cssSelector("div.before-Send")).click();

            //*************************** 下面为短信验证码重试过程 end ***************************

            //截屏
            WebDriverUtil.screenShoter(driver, "TestH5" + "_" + desc + "_succ.png");

            //获取错误文案
            String msg = driver.findElement(By.cssSelector("#app > div > div:nth-child(3) > div.van-dialog__content > div > div:nth-child(3)")).getText();
            System.out.println(">>> 弹框 msg: " + msg);
            Reporter.log(">>> 弹框 msg: " + msg);
            //获取当前url，截取 tid
            String url_current = driver.getCurrentUrl();

            Pattern p = Pattern.compile("tid=(.*?)&");
            Matcher m = p.matcher(url_current);
            if (m.find()) {
                task_id = m.group(1);
            }
            System.out.println(">>> url_current : " + url_current);
            System.out.println(">>> task_id : " + task_id);
            Reporter.log(">>> task_id : " + task_id);

            //关闭进度页面
            driver.findElement(By.cssSelector("i.iconfont.icon-close")).click();

            //检查结果，包括report、raw_data、callback


        } catch (Exception e) {
            System.out.println(e.getMessage());
            Reporter.log(e.getMessage());
            WebDriverUtil.screenShoter(driver, "TestH5" + "_" + desc + "_fail.png");
            assert false;
        } finally {
            driver.close();
            driver.quit();
        }
        return task_id;

    }
}
