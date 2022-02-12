package lj.study.test.h5;

public class TestH5RemoteDriver {
//    static WebDriver driver;
//
//    @BeforeClass
//    public void before() throws MalformedURLException {
//        DesiredCapabilities capability = DesiredCapabilities.chrome();
//        capability.setBrowserName("chrome");
//        capability.setPlatform(Platform.LINUX);
//
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--window-size=1290,1080", "--disable-dev-shm-usage");
//        chromeOptions.addArguments("--disable-extensions");
//
////        capability.setCapability("chromeOptions", chromeOptions);
//        capability.setCapability("goog:chromeOptions", chromeOptions);
//
//        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
//
//    }
//
//    @Test(threadPoolSize = 10, invocationCount = 1)
//    public void test1(){
//        driver.get("http://www.baidu.com");
//        System.out.println(driver.getTitle());
//    }
//
//
//    @AfterClass
//    public void after(){
////        driver.quit();
//        driver.close();
//    }







//    @Test(threadPoolSize = 5, invocationCount = 1)
////    @Test
//    public void test() {
//        DesiredCapabilities capability = DesiredCapabilities.chrome();
//        capability.setBrowserName("chrome");
////        capability.setVersion("70.0.3538.110");
//        capability.setPlatform(Platform.LINUX);
//
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--window-size=1290,1080", "--disable-dev-shm-usage");
//        chromeOptions.addArguments("--disable-extensions");
////
////        capability.setCapability("chromeOptions", chromeOptions);
//        capability.setCapability("goog:chromeOptions", chromeOptions);
//        try {
////            driver = new RemoteWebDriver(new URL("http://172.16.49.33:5555/wd/hub"), capability);
////            driver = new RemoteWebDriver(new URL("http://webdriver.lj-qa.k2-test.ata.info/wd/hub"), capability);
//            driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
//            driver.get("http://www.baidu.com");
//            System.out.println(driver.getTitle());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//            assert false;
//        } finally {
//            driver.close();
//            driver.quit();
//        }
//    }

}
