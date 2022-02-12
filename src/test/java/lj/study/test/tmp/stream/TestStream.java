package lj.study.test.tmp.stream;

import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TestStream {

    @Test
    public void test1() {
        Random random = new Random(10);
        random.ints().limit(10).sorted().forEach(System.out::println);
    }

    @Test
    public void test_flatMap() {
        List<String> list = new ArrayList<>();
        list.add("a1;a2;a3");
        list.add("b1;b2");
        List<String> nl = list.stream()
                .filter(x -> x.indexOf("3") > 0)
                .flatMap(x -> Arrays.stream(x.split(";")))
                .collect(Collectors.toList());
        System.out.println(nl);

        String[] n2 = list.stream()
                .filter(x -> x.indexOf("3") > 0)
                .flatMap(x -> Arrays.stream(x.split(";")))
                .toArray(String[]::new);
        System.out.println(Arrays.toString(n2));

        Set<String> n3 = list.stream()
                .filter(x -> x.indexOf("3") > 0)
                .flatMap(x -> Arrays.stream(x.split(";")))
                .collect(Collectors.toSet());
        System.out.println(n3);

        Map<String, String> n4 = list.stream()
                .filter(x -> x.indexOf("3") > 0)
                .flatMap(x -> Arrays.stream(x.split(";")))
                .collect(Collectors.toMap(String::valueOf, String::toString));
        System.out.println(n4);


    }
}
