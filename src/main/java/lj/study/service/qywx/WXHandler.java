package lj.study.service.qywx;

import com.alibaba.fastjson.JSONObject;
import lj.study.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lj on 2018/6/4 下午4:55
 * Desc: 企业微信开发者文档详见：https://work.weixin.qq.com/api/doc#10167
 */

public class WXHandler {
    private static Logger logger = LoggerFactory.getLogger(WXHandler.class);

    //原始发送账号
    private static String sToken = "9qmS4UFcdgquzgIXB5pQo";
    private static String sEncodingAESKey = "2PuJaTI0UiLoqQuEzTiD0HlsUKhOaCzlRlpRQkoG";

    //企业微信公共号信息
    private static String sCorpID = "wx9e2efe8d9730e164";
    private static int sAgentID = 1000021;
    private static String sSecret = "taIriiy_FyJ6XaJHb_d7_-D3K5EuDqckr2SwHZHAKHQ";


    private static String URL_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static String URL_SEND_MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";


    private static Map<String, WXToken> tokens = new HashMap<>();


    /**
     * 从map
     *
     * @param secret
     * @return
     */
    public static String getAccessTokenFromCache(String secret) {
        WXToken token = tokens.get(secret);
        if (token == null)
            return null;
        else {
            Date now = new Date();
            if (token.getExpiresTime().before(now)) {
                tokens.remove(secret);
                return null;
            } else {
                return token.getToken();
            }
        }
    }

    /**
     * 获取访问token
     *
     * @return
     */
    public static String getAccessToken(String secret) {
        String token = getAccessTokenFromCache(secret);
        if (token != null)
            return token;
        try {
            String rst= HttpUtils.get(String.format(URL_TOKEN, sCorpID, secret));
            if (StringUtils.isNotEmpty(rst)) {
                JSONObject jsonObject = JSONObject.parseObject(rst);
                WXToken accessToken = new WXToken();
                accessToken.setToken(jsonObject.get("access_token").toString());

                // 设置过期时间
                int expireSecond = Integer.parseInt(jsonObject.get("expires_in").toString()) - 200;
                Calendar expireTime = Calendar.getInstance();
                expireTime.add(Calendar.SECOND, expireSecond);
                Date expireDate = expireTime.getTime();
                accessToken.setExpiresTime(expireDate);

                return accessToken.getToken();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定成员、部门发送消息
     *
     * @param message 要发送的消息
     * @return true:发送成功,false:发送失败
     */
    public static boolean sendMessage(WXMessage message) throws Exception {

        message.setAgentId(sAgentID);

        String token = getAccessToken(sSecret);
        if (StringUtils.isEmpty(token))
            return false;
        else {
            String url = String.format(URL_SEND_MESSAGE, token);
            String body_str = JSONObject.toJSONString(message);
            JSONObject body = JSONObject.parseObject(body_str);

            JSONObject headers = new JSONObject();
            headers.put("Content-Type", "application/json;charset=utf-8");

            try {
                String response = HttpUtils.post(url, body, headers);
//                if (response.getStatusCode() == 200) {
//                    JSONObject jsonObject = response.getBodyJson();
//                    if ("0".equals(jsonObject.get("errcode").toString()))
//                        return true;
//                    else {
//                        logger.error("发送消息失败：" + response.getBodyString());
//                        return false;
//                    }
//                } else {
//                    logger.error("发送消息失败：" + response.getBodyString());
//                    return false;
//                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return false;
        }
    }


//    public static void main(String[] args) throws Exception {
//        String actualname="xx";
//
////        TextMessage message = (TextMessage) MessageFactory.createMessage(MessageFactory.TYPE_TEXT);
//        WXMessage message = new WXMessage();
////        message.setAgentId(xx);
//        message.setAgentId(xx);
//        message.setToUser(actualname);
//        message.setToParty("0");
//        message.setText("测试企业微信");
////        message.setTextContent(new TextContent("【jira有人@你】\n【备注内容】 " + commentbody +
////                " \n【Issue】<a href=\""+finalURL+"\">"+summary+"</a> "));
//        WXHandler.sendMessage(message);
//    }

}
