package com.ydj.push.wx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendWeChatUtils {

    //微信企业ID
    private static final String CORPID = "wwede66db4ec023cab";
    //微信企业密匙
    private static final String CORPSECRET = "qnGCY2y6-DOGkSpCQVjyoqRqmos8E6Oi9SIyfwy2ziE";
    //微信应用ID
    private static final int APPLICATION_ID = 1000016;
    //字符编码集
    private static final String CHARSET = "utf-8";

    private CloseableHttpClient httpClient;
    private HttpPost httpPost;//用于提交登陆数据
    private HttpGet httpGet;//用于获得登录后的页面
    private static final String CONTENT_TYPE = "Content-Type";
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Gson gson = new Gson();

    private String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private String sendMessageUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

    /**
     * 微信授权请求，GET类型，获取授权响应，用于其他方法截取token
     *
     * @param Get_Token_Url
     * @return String 授权响应内容
     * @throws IOException
     */
    public String toAuth(String Get_Token_Url) throws IOException {

        httpClient = HttpClients.createDefault();
        httpGet = new HttpGet(Get_Token_Url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String resp;
        try {
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return resp;
    }

    public String getToken(String corpid, String corpsecret) throws IOException {
        SendWeChatUtils sw = new SendWeChatUtils();

        String resp = sw.toAuth(String.format(getTokenUrl,corpid,corpsecret));

        System.out.println(resp+" ?????????????????");

        Map<String, Object> map = gson.fromJson(resp,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        return map.get("access_token").toString();
    }


    public String createPostData(String touser, String msgtype, String contentKey, String contentValue) {
        IacsWeChatDataVo wcd = new IacsWeChatDataVo();
        wcd.setTouser(touser);
        wcd.setAgentid(APPLICATION_ID);
        wcd.setMsgtype(msgtype);
        Map<Object, Object> content = new HashMap<Object, Object>();
        content.put(contentKey, contentValue + "\n--------\n" + df.format(new Date()));
        wcd.setText(content);
        return gson.toJson(wcd);
    }

    public String post(String charset, String contentType,
                       String data) throws IOException {

        String token = getToken(CORPID, CORPSECRET);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        httpPost = new HttpPost(sendMessageUrl + token);
        httpPost.setHeader(CONTENT_TYPE, contentType);
        httpPost.setEntity(new StringEntity(data, charset));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String resp;
        try {
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return resp;
    }

    public String toUser(String[] receiver) {
        String[] arr = this.toString(receiver).split(",");
        StringBuffer sb = new StringBuffer();
        //将不含有@符号的邮箱地址取出
        for (String str : arr) {
           sb.append(str).append("|");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }


    public String toString(String[] array) {
        StringBuffer sb = new StringBuffer();
        for (String str : array) {
            sb.append(str + ",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
}
