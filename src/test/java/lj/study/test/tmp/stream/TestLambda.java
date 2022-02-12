package lj.study.test.tmp.stream;

import org.testng.annotations.Test;

import java.util.Arrays;

public class TestLambda {
    @Test
    public void test() {
//        final int a = 10;
        int[] ints = {1, 2, 3, 4, 5, 6, 7, 8};
        int count = (int) Arrays.stream(ints).filter(i -> i % 2 == 0).count();
        int s = (int) Arrays.stream(ints).reduce(1, (x, y) -> x * y);
        System.out.println(count);
        System.out.println(s);
    }
}
