package lj.study.test.tmp.testng;

import org.testng.annotations.BeforeTest;

public class Before {
    @BeforeTest
    public void test(){
        System.out.println("Before Test1");
    }
}
