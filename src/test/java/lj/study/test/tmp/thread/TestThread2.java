package lj.study.test.tmp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestThread2 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @DataProvider(name = "dp", parallel = true)
    public Object[][]  dp(){
        return new Object[][]{
                {"a"},
                {"b"},
                {"c"},
                {"d"},
                {"e"},
                {"f"},
                {"g"}
        };
    }

    @Test(dataProvider = "dp")
    public void test(String tag){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(tag);
    }

    @Test(dataProvider = "dp")
    public void test2(String tag){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("2"+tag);
    }

    @Test(dataProvider = "dp")
    public void test3(String tag){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("3"+tag);
    }
}
