package com.javacode.service;


import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.entity.News;
import com.javacode.mapper.NewsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新闻相关的service
 */
@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;

    /**
     * 分页查询新闻列表
     */
    public PageInfo<News> findPage(Integer pageNum, Integer pageSize, String title){
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsMapper.findByTitle(title);
        return PageInfo.of(list);
    }

    /**
     * 页面传来的上传文件列表转换成以逗号隔开的id列表
     */
    private void convertFileListToField(News news){
        List<Long> fileList = news.getFileList();
        if(!CollectionUtil.isEmpty(fileList)){
            news.setPicture(fileList.toString());
        }
    }
    /**
     * 新增新闻
     */
    public News add(News news){
//        convertFileListToField(news);
//        news.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        newsMapper.insertSelective(news);
        return news;
    }
    /**
     * 修改新闻
     */
    public void update(News news){
        convertFileListToField(news);
        news.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        newsMapper.updateByPrimaryKeySelective(news);
    }

    /**
     * 根据id删除新闻
     */
    public void delete(Long id){
        newsMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条新闻
     */
    public News findById(Long id){
        return newsMapper.selectByPrimaryKey(id);
    }

    /**
     * 增加点击次数
     */
    public void incrementClickCount(Long id) {
        News news = newsMapper.findById(id);
        if (news != null) {
            Long clickCount = news.getClick();
            if (clickCount == null) {
                clickCount = 0L; // 如果click字段为空，初始化为0
            }
            clickCount++; // 增加点击次数
            news.setClick(clickCount);
            newsMapper.updateClickCount(id);
        }
    }

    /**
     * 增加新闻的点赞数
     * @param id 新闻ID
     */
    public void incrementLikeCount(Long id) {
        // 根据ID查询新闻
        News news = newsMapper.findById(id);
        Long likeCount = news.getLikes();
        if (likeCount == null) {
            likeCount = 0L; // 如果likes字段为空，初始化为0
        }
        likeCount++; // 增加点赞次数
        news.setLikes(likeCount);
        newsMapper.updateLikeCount(id);
    }

    /**
     * 获取新闻的浏览数
     * @param id 新闻ID
     * @return 浏览数
     */
    public int getClickCount(Long id) {
        News news = newsMapper.findById(id);
        if (news != null) {
            Long clickCount = news.getClick();
            return clickCount != null ? clickCount.intValue() : 0;
        }
        return 0; // 如果新闻不存在或者浏览数为空，返回0
    }

    /**
     * 获取新闻的点赞数
     * @param id 新闻ID
     * @return 点赞数
     */
    public int getLikeCount(Long id) {
        News news = newsMapper.findById(id);
        if (news != null) {
            Long likeCount = news.getLikes();
            return likeCount != null ? likeCount.intValue() : 0;
        }
        return 0; // 如果新闻不存在或者点赞数为空，返回0
    }


    //根据疾病信息检索新闻
    public List<News> getNewssByDisease(String diseaseName) {
        return newsMapper.findByDiseaseName(diseaseName);
    }

    //获取所有新闻
    public List<News> getAllNewss() {
        return newsMapper.getAllNewss();
    }

    public void initNewsList() {
        newsList = getAllNewss();
    }
    public void loadNewsList() {
        // 从数据库中加载新闻列表
        this.newsList = newsMapper.getAllNewss();
    }



    private List<News> newsList = new ArrayList<>(); // 初始化为一个空列表
    // 获取同类型的新闻列表
    public List<News> getNewssByType(String type) {
        List<News> newssByType = new ArrayList<>();
        for (News news : newsList) {
            String newsType = news.getType();
            if (newsType != null && newsType.equals(type)) {
                newssByType.add(news);
            }
        }
        return newssByType;
    }

    /**
     * 更新新闻记录的关键字字段
     * @param newsId 新闻ID
     * @param keywords 关键字字符串
     */
    public void updateKeywords(Long newsId, String keywords) {
        // 输出传递给方法的参数值
        System.out.println("Received news ID: " + newsId);
        System.out.println("Received keywords: " + keywords);

        // 查询对应的新闻记录
        News news = newsMapper.findById(newsId);

        // 输出获取的新闻记录情况
        if (news != null) {
            System.out.println("Retrieved news: " + news);
            // 更新关键字字段
            news.setKeywords(keywords);
            newsMapper.updateByPrimaryKeySelective(news);
        } else {
            // 如果找不到对应的新闻记录，输出错误消息
            System.err.println("找不到ID为 " + newsId + " 的新闻记录");
        }
    }

    public List<String> getAllNewsTypes() {
        return newsMapper.getAllNewsTypes();
    }

    public String getContent(Long newsId) {
        News news = newsMapper.findById(newsId);
        if (news != null) {
            return news.getContent();
        } else {
            // 如果找不到对应的新闻记录，返回空字符串或者抛出异常，根据实际情况处理
            return ""; // 或者抛出自定义异常，如 throw new NewsNotFoundException("News not found with ID: " + newsId);
        }
    }

    /**
     * 将关键字存入数据库的 keyword 字段中
     * @param newsContent 新闻内容
     * @param keywords 关键字字符串
     */
    public void updateKeywords(String newsContent, String keywords) {
        // 创建一个新的 News 对象
        News news = new News();
        // 设置新闻内容
        news.setContent(newsContent);
        // 设置关键字
        news.setKeywords(keywords);
        // 将新闻对象插入数据库
        newsMapper.insertSelective(news);
    }
    /**
     * 保存新闻对象到数据库
     * @param news 新闻对象
     * @return 保存后的新闻对象
     */
    public News saveNews(News news) {
        newsMapper.insertSelective(news);
        return news;
    }

    public News getNewsById(Long newsId) {
        return newsMapper.selectByPrimaryKey(newsId);
    }

    public List<News> findByTitle(String title) {
        // 调用持久层方法实现模糊查询
        return newsMapper.findByTitleContaining(title);
    }


}
