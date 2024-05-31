package com.javacode.controller;

import com.javacode.entity.News;
import com.javacode.service.NewsService;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Comparator;

@RestController
public class BaiduKeywordController {

    public static final String API_KEY = "K3CAmeZcToMOBBPp3A2LvrMm";
    public static final String SECRET_KEY = "vAgkl7KP01SWgkSmgrJftD8TigKnGjk4";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Autowired
    private NewsService newsService;

    @PostMapping("/keyword")
    public String extractKeywords(@org.springframework.web.bind.annotation.RequestBody String newsContent) throws IOException {
        System.out.println("Received POST request to /keyword");

        String accessToken = getAccessToken();
        System.out.println("Access token obtained: " + accessToken);

        // 添加调试语句，查看请求体是否正确解析
        System.out.println("Received news content: " + newsContent);

        // 提取文本内容
        JSONObject json = new JSONObject(newsContent);
        String title = json.getString("title"); // 获取标题
        String content = json.getString("content");
        // 创建一个新的News对象，并将关键字存入
        News news = new News();
        news.setContent(content); // 设置内容
        // 去除 HTML 标签
        content = removeHtmlTags(content);

        // 添加调试语句，打印提取的文本内容
        System.out.println("Extracted content: " + content);

        List<String> keywords = getKeywords(content, accessToken);
        System.out.println("Extracted keywords: " + keywords);

        // 将前四个关键字存入News表的keyword字段中
        StringBuilder keywordBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(keywords.size(), 4); i++) {
            keywordBuilder.append(keywords.get(i));
            if (i < 3) {
                keywordBuilder.append(", ");
            }
        }
        String topKeywords = keywordBuilder.toString();

        news.setKeywords(topKeywords);
        // 创建一个新的News对象，并设置标题和内容
        news.setTitle(title); // 设置标题
        news.setKeywords(topKeywords); // 设置关键字
        // 设置时间为当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = formatter.format(new Date());
        news.setTime(currentTime);
        // 保存新闻对象到数据库
        newsService.saveNews(news);

        // 构建 JSON 对象，包含提取的关键字和文本内容
        JSONObject responseJson = new JSONObject();
        responseJson.put("content", content);
        responseJson.put("keywords", keywords);

        // 返回 JSON 数据字符串
        return responseJson.toString();
    }

    private String removeHtmlTags(String content) {
        // 使用正则表达式去除 HTML 标签
        return content.replaceAll("\\<.*?\\>", "");
    }


    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to obtain access token: " + response);
            }
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);

            return jsonObject.getString("access_token");
        }
    }

    private List<String> getKeywords(String content, String accessToken) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject requestBody = new JSONObject();
        JSONArray textArray = new JSONArray();
        textArray.put(content);
        requestBody.put("text", textArray); // 修改键名为 "texts"，表示文本数组

        // 添加调试语句，输出请求内容
        System.out.println("Request body: " + requestBody.toString());

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/txt_keywords_extraction?access_token=" + accessToken)
                .method("POST", RequestBody.create(mediaType, requestBody.toString()))
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to extract keywords: " + response);
            }
            String responseBody = response.body().string();
            // 添加调试语句，输出响应内容
            System.out.println("Response body: " + responseBody);

            JSONObject jsonObject = new JSONObject(responseBody);

            // 添加调试语句，输出响应中的 JSON 结构
            System.out.println("Response JSON: " + jsonObject.toString());

            JSONArray resultsArray = jsonObject.getJSONArray("results");

            List<String> keywords = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);
                String keyword = resultObj.getString("word");
                keywords.add(keyword);
            }
            return keywords;
        }
    }


}