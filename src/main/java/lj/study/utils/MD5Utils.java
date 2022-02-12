package lj.study.utils;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MD5Utils {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    /**
     * Spring中的DigestUtils工具类,MD5加密
     * @param dataStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String generateMD5(String dataStr){
        String base = dataStr + slat;

        String md5 = null;
        try {
            md5 = DigestUtils.md5DigestAsHex(base.getBytes("UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * 获取文件md5值
     * @param filePath
     * @return
     */
    public static String generateMD54File(String filePath){
        String s = null;
        try {
            s = DigestUtils.md5DigestAsHex(MD5Utils.class.getResourceAsStream(filePath));
        }catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
