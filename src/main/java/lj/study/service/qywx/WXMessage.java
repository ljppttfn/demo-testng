package lj.study.service.qywx;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by lj on 2018/6/4 下午4:56
 * Desc: 企业微信开发者文档详见：https://work.weixin.qq.com/api/doc#10167
 */
public class WXMessage {
    @JSONField(name = "touser")
    private String toUser;

    @JSONField(name = "toparty")
    private String toParty;

    @JSONField(name = "totag")
    private String toTag;

    //touser、toparty、totag不能同时为空, 多个接收者用‘|’分隔

    @JSONField(name = "agentid")
    private long agentId;

    //消息类型，此时固定为：text
    //其他消息类型及内容格式，请参考接口文档
    @JSONField(name = "msgtype")
    private String msgType = "text";

    //消息内容，最长不超过2048个字节
    @JSONField(name ="text")
    private JSONObject text;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String touser) {
        this.toUser = touser;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toparty) {
        this.toParty = toparty;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgtype) {
        this.msgType = msgtype;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentid) {
        this.agentId = agentid;
    }


    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public JSONObject getText() {
        return text;
    }

    public void setText(String content) {
        JSONObject text = new JSONObject();
        text.put("content", content);
        this.text = text;
    }
}
