//package com.javacode.service;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class BaiduKeywordService {
//
//    // API密钥
//    private static final String API_KEY = "K3CAmeZcToMOBBPp3A2LvrMm";
//    public static final String SECRET_KEY = "vAgkl7KP01SWgkSmgrJftD8TigKnGjk4";
//
//    // 百度关键词提取API端点
//    private static final String BAIDU_KEYWORD_ENDPOINT = "https://aip.baidubce.com/rpc/2.0/nlp/v1/txt_keywords_extraction";
//
//    private final RestTemplate restTemplate;
//
//    public BaiduKeywordService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public List<String> extractKeywords(String content) {
//        try {
//            // 获取访问令牌
//            String accessToken = getAccessToken();
//            // 构造HTTP请求头
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("Authorization", "Bearer " + accessToken);
//
//            // 构造请求体
//            String requestBody = "{\"text\": \"" + content + "\"}";
//
//            // 构造HTTP实体
//            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//            // 发送HTTP请求
//            ResponseEntity<String> response = restTemplate.exchange(BAIDU_KEYWORD_ENDPOINT, HttpMethod.POST, entity, String.class);
//
//            // 打印完整的响应内容
//            System.out.println("Response body: " + response.getBody());
//
//            // 处理API响应
//            if (response.getStatusCode().is2xxSuccessful()) {
//                // 解析响应并返回关键字列表
//                // 你需要编写代码解析响应并提取关键字列表，然后返回该列表
//                // 这里假设API的响应是一个JSON格式的字符串，你可以使用JSON解析库来解析它
//                // 这里只是示例代码，你需要根据实际情况修改
//                return parseKeywordsFromResponse(response.getBody());
//            } else {
//                // 处理API调用失败的情况
//                // 这里需要根据实际情况进行处理
//                return null;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 处理异常
//            return null;
//        }
//    }
//
//    // 获取百度AI平台的访问令牌
//    private String getAccessToken() throws IOException {
//        OkHttpClient client = new OkHttpClient();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=" + API_KEY + "&client_secret=" + SECRET_KEY + "&grant_type=client_credentials")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = client.newCall(request).execute();
//        if (response.isSuccessful()) {
//            String responseBody = response.body().string();
//            // 解析响应获取Access Token
//            return new JSONObject(responseBody).getString("access_token");
//        } else {
//            throw new IOException("Unexpected code " + response);
//        }
//    }
//
//    // 这是一个示例方法，你需要根据实际情况编写代码来解析响应并提取关键字列表
//    private List<String> parseKeywordsFromResponse(String responseBody) {
//        // 解析JSON字符串并提取关键字列表
//        // 这里只是示例代码，你需要根据实际情况编写
//        return null;
//    }
//}
