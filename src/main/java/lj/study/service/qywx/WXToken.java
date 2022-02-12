package lj.study.service.qywx;

import java.util.Date;

/**
 * Created by lj on 2018/6/4 下午4:56
 * Desc: 企业微信开发者文档详见：https://work.weixin.qq.com/api/doc#10167
 */
public class WXToken {
    private String token;
    private Date expiresTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }
}
