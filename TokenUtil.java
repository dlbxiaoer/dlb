

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付连接类
 * @author zhaoyz
 *
 */
public class TokenUtil {

    
    
    public static void main(String[] args) throws Exception {
       // connect();
        
        
    }
    
    private static void connect() throws Exception{
        final String path="/v1/agent/order/wx/create";
        final String secretKey="d9bb7fc0493647a48e937ded073e342280dd45ac";
        final String accessKey="e84fab7878194925a28f5ee8e926e77f913cc646";
//        final String timestamp=String.valueOf(Instant.now().toEpochMilli());
        final String timestamp = String.valueOf(System.currentTimeMillis());
        URL url = new URL("http://openapi.duolabao.cn"+path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false); 
        JSONObject body=new JSONObject();
        body.put("agentNum", "10001014472963095391100");
        body.put("amount", "0.01");
        body.put("callbackUrl", "www.duolabao.cn");
//        json.put("completeUrl", "http://zs.easyorder.cn/weixin/index/paysuccess.html");
        body.put("customerNum", "10001114533598995853210");
        body.put("shopNum", "10001214696702060035149");
        body.put("requestNum", "0706161456963095391123"); //注：必须为18-32位纯数字
        body.put("source", "API");
        
        System.out.println(timestamp);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("accessKey", accessKey);
        urlConnection.setRequestProperty("timestamp", timestamp);
        urlConnection.setRequestProperty("token",getSignStr(secretKey, timestamp, path, body.toJSONString()));//.toJSONString()
        System.out.println(urlConnection.getRequestProperty("token"));
        urlConnection.connect();
        urlConnection.getOutputStream().write(body.toJSONString().getBytes());
        urlConnection.getOutputStream().flush();
        urlConnection.getOutputStream().close();
        try{
            InputStream in=urlConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count = -1;
            while((count = in.read(data,0,1024)) != -1)
                out.write(data, 0, count);
            data = null;
            String msg=new String(out.toByteArray());
            System.out.println(msg);
        }catch (Exception e) {
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
  public static String getSHA1(String msg){
      MessageDigest md5 = null;
      try {
          System.out.println(msg);
          md5 = MessageDigest.getInstance("SHA-1");
          md5.update(msg.getBytes());
          byte[] buffer=md5.digest();
          StringBuffer sb=new StringBuffer(buffer.length*2);
          for(int i=0;i<buffer.length;i++){
              sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
              sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
          }
          return sb.toString().toUpperCase();
      } catch (Exception e) {
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
        signStr.append("secretKey=").append(secretKey)
            .append("&timestamp=").append(timestamp)
            .append("&path=").append(path);
        if(body!=null && !body.isEmpty())
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }
    
    private static final String byteToHex(byte[] bytes){
        StringBuffer sb=new StringBuffer(bytes.length*2);
        for(int i=0;i<bytes.length;i++){
            sb.append(Character.forDigit((bytes[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(bytes[i] & 0x0f, 16));
        }
        return sb.toString();
    }

}
