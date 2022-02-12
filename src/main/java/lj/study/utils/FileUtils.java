package lj.study.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    /**
     * 从resource目录下读取文件，采用流方式，支持jar包运行方式下的读取
     * @param path resources目录下的文件路径，如 /jsch/id_rsa
     * @return String
     */
    public static String readFrom(String path){
//        InputStream inputStream = FileUtils.class.getResourceAsStream("/jsch/id_rsa");
        InputStream inputStream = FileUtils.class.getResourceAsStream(path);
        try {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
