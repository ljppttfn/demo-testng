package lj.study.test.tmp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestTestNGParallel {
    private static final Logger logger = LoggerFactory.getLogger(TestTestNGParallel.class);

    @Test(dataProvider = "test_parallel")
    public void test(int str1, int str2,int str3, int str4){
        logger.info("00: "+String.valueOf(str1));
        try{
            Thread.sleep(str1 * 2000L);
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("01: "+String.valueOf(str1));
    }

    @DataProvider(name = "test_parallel", parallel = true)
    private Object[][] test_parallel(){
        Object[][] dp = new Object[15][];
        for(int i=0; i<15; i++){
            dp[i] = new Object[]{i, 0, 0, 0};
        }
        return dp;
    }
}
