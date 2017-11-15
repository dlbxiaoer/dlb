package com.duolabao.inter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付连接类
 * @author zhaoyz
 *
 */
public class DlbCustomerB2C {

    public static void main(String[] args) throws Exception {
        connect();
    }

    // 被扫接口地址：http://openapi.duolabao.cn/v1/agent/passive/create
    private static void connect() throws Exception {
        //        final String path="/v1/customer/order/payurl/create";
        final String path = "/v1/customer/passive/create";
        final String secretKey = "dba2df87597440fd8c0444dec3b65262dcbf3f38";
        final String accessKey = "62694e2e777844e39e2ddb75869ce8f9c0f999b8";
        final String timestamp = String.valueOf(System.currentTimeMillis());
        System.out.println("timestamp:" + timestamp);
        URL url = new URL("https://openapi.duolabao.com" + path);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setHostnameVerifier(new Verifier());
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        JSONObject json = new JSONObject();
        json.put("customerNum", "10001114533598995853210");
        json.put("amount", "0.01");
        json.put("callbackUrl", "www.duolabao.cn");
        json.put("authCode", "130182615609710859");
        json.put("shopNum", "10001214696702060035149");
        json.put("requestNum", "10021115099651035129885");//必须是 18--32 位的纯数字
        json.put("source", "API");

        System.out.println(timestamp);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("accessKey", accessKey);
        urlConnection.setRequestProperty("timestamp", timestamp);
        urlConnection.setRequestProperty("token", getSignStr(secretKey, timestamp, path, json.toJSONString()));
        System.out.println(json.toJSONString());
        System.out.println(urlConnection.getRequestProperty("token"));
        urlConnection.connect();
        urlConnection.getOutputStream().write(json.toJSONString().getBytes());
        urlConnection.getOutputStream().flush();
        urlConnection.getOutputStream().close();
        try {
            InputStream in = urlConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count = -1;
            while ((count = in.read(data, 0, 1024)) != -1)
                out.write(data, 0, count);
            data = null;
            String msg = new String(out.toByteArray());
            System.out.println(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * {生成签名1}
     * 
     * @param msg
     * @return
     * @author: zhaoyz
     */
    public static String getSHA1(String msg) {
        MessageDigest md5 = null;
        try {
            System.out.println(msg);
            md5 = MessageDigest.getInstance("SHA-1");
            md5.update(msg.getBytes());
            byte[] buffer = md5.digest();
            StringBuffer sb = new StringBuffer(buffer.length * 2);
            for (int i = 0; i < buffer.length; i++) {
                sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
            }
            return sb.toString().toUpperCase();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成签名2
     * @param secretKey 密钥
     * @param timestamp 时间戳
     * @param path 路径
     * @param body 消息体 {can null}
     * @return sha1签名
     */
    private static String getSignStr(String secretKey, String timestamp, String path, String body) {
        StringBuilder signStr = new StringBuilder();
        signStr.append("secretKey=").append(secretKey).append("&timestamp=").append(timestamp).append("&path=")
                .append(path);
        if (body != null && !body.isEmpty())
            signStr.append("&body=").append(body);
        return sha1(signStr.toString()).toUpperCase();
    }

    /**
     * SHA-1加密
     * @param sourceStr
     * @return
     */
    public static final String sha1(String sourceStr) {
        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sourceStr.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static final String byteToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Character.forDigit((bytes[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(bytes[i] & 0x0f, 16));
        }
        return sb.toString();
    }

}

