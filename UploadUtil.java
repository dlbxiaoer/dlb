/*
 * Copyright 2013-2015 duolabao.com All right reserved. This software is the
 * confidential and proprietary information of duolabao.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with duolabao.com.
 */
package com.duolabao.base.upload;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 类UploadUtil的实现描述：TODO 类实现描述
 *
 * @author chengdong.wei 2016年09月24日 16:53:46
 */
public class UploadUtil {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("C:/01.jpg");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        int length = bufferedInputStream.read(bytes);
        while (length != -1) {
            baos.write(bytes, 0, length);
            length = bufferedInputStream.read(bytes);
        }
        baos.flush();

        byte[] fileBytes = baos.toByteArray();

        DeclareUploadVO declareUploadVO = new DeclareUploadVO();
        declareUploadVO.setFile(fileBytes);
        declareUploadVO.setCustomerNum("10001114745491441190171");
        declareUploadVO.setAttachType("PROTOCAL");
        String jsonStr = JSON.toJSONString(declareUploadVO);

        System.out.println(jsonStr);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:80801/v1/agent/declare/attach/upload");
        httpPost.setHeader("content-Type","application/json");
        StringEntity stringEntity = new StringEntity(jsonStr);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String s = EntityUtils.toString(entity);
            System.out.println(s);
        }
    }
}
