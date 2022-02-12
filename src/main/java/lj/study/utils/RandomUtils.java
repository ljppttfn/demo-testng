package lj.study.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtils {

    public static String randomID(){
        return RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

}
