package lj.study.test.tmp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TestThread1 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test1(){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test1");
    }

    @Test
    public void test2(){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test2");
    }

    @Test
    public void test3(){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test3");
    }

    @Test
    public void test4(){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test4");
    }

    @Test
    public void test5(){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test5");
    }

    @Test
    public void test6(){
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test6");
    }
}
