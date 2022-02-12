package lj.study.test.tmp.testng;

import org.testng.annotations.BeforeTest;

public class Before2 {
    @BeforeTest(groups = {"group2"})
    public void test(){
        System.out.println("Before Test2");
    }
}
