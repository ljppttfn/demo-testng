package lj.study.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 基于Druid连接池的mysql工具类。
 * 注意：使用连接池工具类后去conn后，需要手动关闭，否则conn不释放，连接池满后将无法再创建连接
 */
@Data
public class MySQLUtils {
    private static final Logger logger = LoggerFactory.getLogger(MySQLUtils.class);
    
//    private String jdbc_driver = "com.mysql.cj.jdbc.Driver";
    private String jdbc_url;
    String username;
    String password;
    DruidDataSource dds;

    public MySQLUtils(String jdbc_url, String username, String password) {
        this.jdbc_url = jdbc_url;
        this.username = username;
        this.password = password;

        logger.info("创建Druid数据源... " + jdbc_url);
        dds = new DruidDataSource();
        dds.setUrl(jdbc_url);
        dds.setUsername(username);
        dds.setPassword(password);


        dds.setInitialSize(5);
        dds.setMaxActive(10);
        dds.setMaxWait(5000);
    }

    /**
     * 获取连接池的连接，使用完后需要关闭，按顺序依次关闭ResultSet、Statement、Connection
     * @return DruidPooledConnection
     */
    public DruidPooledConnection getConnection(){
        try {
            return dds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void querySample(String sql) {
        try {
            DruidPooledConnection conn = dds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                logger.info(rs.toString());
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSample(String sql) {
        try {
            DruidPooledConnection conn = dds.getConnection();
            Statement stmt = conn.createStatement();
            int count = stmt.executeUpdate(sql);
            logger.info("执行 update sql，受影响的行数：" + count);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        logger.info("关闭Druid数据源...");
        if(dds != null){
            dds.close();
        }
    }

}
