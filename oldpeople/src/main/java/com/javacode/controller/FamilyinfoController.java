package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.*;
import com.javacode.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 家属增删改查控制器
 */
@RestController
@RequestMapping(value = "/familyinfo")
public class FamilyinfoController {

    @Resource
    private FamilyinfoService familyinfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private OldinfoService oldinfoService;
    @Autowired
    private HistoryService historyService;


    /**
     * 分页查询家属列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param name 家属名字
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<Familyinfo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "15") Integer pageSize,
                                       @PathVariable String name){
        return Result.success(familyinfoService.findPage(pageNum,pageSize,name));
    }

    /**
     * 新增家属
     */
    //@PutMapping用于修改
    @PostMapping
    public Result<Familyinfo> add(@RequestBody Familyinfo familyinfo) {
        try {
            // 调用service层的方法添加家属信息
            familyinfoService.add(familyinfo);
            // 在新增家属的同时，将家属的信息存入user表中，并将type字段设置为家属
            User user = new User();
            user.setId(familyinfo.getFno());
            user.setName(familyinfo.getName());
            user.setPhone(familyinfo.getPhone());
            user.setType("家属");
            userService.add(user); // 假设userService中有添加用户的方法

            return Result.success(familyinfo);
        } catch (DuplicateKeyException e) {
            // 处理ID冲突异常
            return Result.error("2007","ID冲突，请重试或联系管理员");
        }
    }


    /**
     * 更新家属
     */
    @PutMapping
    public Result update(@RequestBody Familyinfo familyinfo){
        familyinfoService.update(familyinfo);  //调用service层的方法
        // 同步更新到 user 表中 根据id匹配需要更新的家属信息
        User user = userService.getUserById(familyinfo.getFno());
        if (user != null) {
            user.setName(familyinfo.getName());
            user.setPhone(familyinfo.getPhone());
            // 更新用户信息
            userService.update(user);
        }
        return Result.success();
    }

    /**
     * 删除家属
     */
    @DeleteMapping("/{fno}")
    public Result delete(@PathVariable Integer fno){
        familyinfoService.delete(fno);
        userService.delete(fno);
        return Result.success();
    }

    /**
     * 新闻推荐
     */
    @Autowired
    private NewsService newsService;
    @Autowired
    private OldinfoService oldInfoService;
    @Autowired
    private IllnessService illnessService;

    @GetMapping("/news")
    public List<News> getRecommendedNewssForUser(@RequestParam("userId") Long userId) {
        // 加载所有新闻列表
        List<News> allNewsList = newsService.getAllNewss();

        // 加载新闻列表
        newsService.loadNewsList();

        // 根据用户ID获取家属信息，家属id和用户id是一一对应的
        Familyinfo familyinfo = familyinfoService.getFamilyInfoByUserId(userId);
        if (familyinfo == null) {
            return new ArrayList<>(); // 返回空列表，表示没有推荐新闻
        }

        // 获取用户对应的老人信息的 ID 列表
        List<Long> oldInfoIds = familyinfo.getOldInfoIds();

        // 存储所有老人的疾病类型
        Set<String> allIllnessTypes = new HashSet<>();

        // 根据老人信息的 ID 列表直接查询老人信息
        List<Oldinfo> oldInfoList = oldInfoService.findOldinfosByIds(oldInfoIds);
        System.out.println("老人信息列表: " + oldInfoList);

        // 遍历所有老人，将疾病类型加入到集合中得出疾病类型表
        for (Oldinfo oldInfo : oldInfoList) {
            List<Long> illnessIds = oldInfo.getIllnessIds();
            if (illnessIds != null && !illnessIds.isEmpty()) {
                for (Long illnessId : illnessIds) {
                    Illness illness = illnessService.getIllnessById(illnessId);
                    if (illness != null) {
                        allIllnessTypes.add(illness.getName());
                    }
                }
            }
        }
        System.out.println("疾病类型列表: " + allIllnessTypes);

        // 计算疾病相关新闻的数量
        int diseaseNewsCount = allIllnessTypes.size();
        System.out.println("Disease news count: " + diseaseNewsCount);


        // 获取用户历史记录关键词列表
        List<String> userHistoryKeywords = historyService.getUserHistoryKeywords(userId);
        System.out.println("用户历史记录关键词列表: " + userHistoryKeywords);

        // 根据用户历史记录关键字筛选新闻
        List<News> filteredNewss = filterNewsByHistoryKeywords(allNewsList, userHistoryKeywords);
        System.out.println("用户历史记录关键字筛选新闻: " + filteredNewss);

        // 计算关键字推送的数量
        int keywordPushCount = filteredNewss.size();

        // 计算疾病推送的数量
        int illnessPushCount = allIllnessTypes.size();

        // 计算总推荐列表数量
        int totalRecommendedNewsCount = keywordPushCount + illnessPushCount;

        // 存储推荐的新闻
        Set<News> recommendedNewss = new HashSet<>();

        // 存储推荐新闻的 ID
        Set<Long> recommendedNewsIds = new HashSet<>();

        // 判断是否有关键字新闻被推荐
        boolean keywordNewsRecommended = !filteredNewss.isEmpty();

        // 添加健康生活等四种类型的新闻
        List<News> healthCategoryNews = new ArrayList<>();
        for (String category : HEALTH_CATEGORIES) {
            List<News> categoryNews = newsService.getNewssByType(category);
            if (!categoryNews.isEmpty()) {
                healthCategoryNews.addAll(categoryNews);
                // 输出每个健康生活类别的新闻数量
                System.out.println("Number of " + category + " news: " + categoryNews.size());
            }
        }
        System.out.println("Health category news: " + healthCategoryNews);




        // 存储推荐的疾病相关新闻数量
        int recommendedDiseaseNewsCount = 0;

        // 根据整合后老人的疾病类型列表，获取对应类型的新闻并进行推荐
        for (String illnessType : allIllnessTypes) {
            List<News> newsList = newsService.getNewssByType(illnessType);
            if (!newsList.isEmpty()) {
                // 根据点赞量和浏览量进行排序
                Collections.sort(newsList, (news1, news2) -> {
                    // 计算新闻的综合权重
                    double weight1 = calculateWeight(news1);
                    double weight2 = calculateWeight(news2);

                    // 比较新闻的权重
                    return Double.compare(weight2, weight1);
                });

                // 遍历新闻列表，确保每篇新闻只添加一次
                for (News news : newsList) {
                    if (!recommendedNewsIds.contains(news.getId())) {
                        // 调试语句：打印要添加的新闻标题以及推荐列表中已有的新闻标题
                        System.out.println("Adding news: " + news.getTitle());
                        System.out.println("推荐列表中已有的新闻标题: " + recommendedNewss.stream().map(News::getTitle).collect(Collectors.toList()));

                        recommendedNewss.add(news);
                        recommendedNewsIds.add(news.getId());
                        recommendedDiseaseNewsCount++; // 每添加一条疾病相关新闻，计数加一
                        System.out.println("添加疾病类型新闻: " + illnessType);
                    }
                }
            } else {
                System.out.println("没有这种疾病类型新闻: " + illnessType);
            }
        }

        System.out.println("疾病相关新闻推荐数量: " + recommendedDiseaseNewsCount);
        // 计算要添加的健康生活类别新闻的数量
        int healthCategoryNewsToAdd = calculateHealthCategoryNewsToAdd(keywordNewsRecommended, totalRecommendedNewsCount, filteredNewss, allIllnessTypes, recommendedDiseaseNewsCount);
        // 添加健康生活类别的新闻到推荐列表
        addRandomHealthCategoryNews(recommendedNewss, recommendedNewsIds, healthCategoryNews, healthCategoryNewsToAdd);

        // 将筛选后的新闻加入推荐列表，避免重复添加
        for (News news : filteredNewss) {
            if (!recommendedNewsIds.contains(news.getId())) {
                recommendedNewss.add(news);
                recommendedNewsIds.add(news.getId());
            }
        }

        // 转换为List并返回
        return new ArrayList<>(recommendedNewss);
    }

    // 健康四个类别的数组
    private static final String[] HEALTH_CATEGORIES = {"健康生活", "健康饮食", "养生方式", "健身运动"};

    // 添加健康生活类别的新闻到推荐列表
    private void addRandomHealthCategoryNews(Set<News> recommendedNewss, Set<Long> recommendedNewsIds, List<News> healthCategoryNews, int healthCategoryNewsToAdd) {
        Random random = new Random();
        int categoryCount = HEALTH_CATEGORIES.length;

        // 计算每个类别平均应该添加的新闻数量
        int averageNewsPerCategory = healthCategoryNewsToAdd / categoryCount;

        // 计算剩余的名额
        int remainingNews = healthCategoryNewsToAdd - (averageNewsPerCategory * categoryCount);

        // 循环添加新闻到各个类别
        for (String category : HEALTH_CATEGORIES) {
            // 找到该类别下的所有新闻
            List<News> categoryNews = healthCategoryNews.stream()
                    .filter(news -> news.getType().equals(category))
                    .collect(Collectors.toList());

            // 计算该类别需要添加的新闻数量
            int newsToAdd = averageNewsPerCategory;

            // 如果有剩余的名额，则随机抽取剩下的新闻
            if (remainingNews > 0) {
                newsToAdd++;
                remainingNews--;
            }

            // 添加新闻到该类别
            while (newsToAdd > 0 && !categoryNews.isEmpty()) {
                News selectedNews = categoryNews.remove(random.nextInt(categoryNews.size()));
                addNewsToList(selectedNews, recommendedNewss, recommendedNewsIds);
                newsToAdd--;
            }
        }
    }

    // 将新闻添加到推荐列表
    private void addNewsToList(News news, Set<News> recommendedNewss, Set<Long> recommendedNewsIds) {
        if (!recommendedNewsIds.contains(news.getId())) {
            System.out.println("四个类别的新闻: " + news.getTitle());
            recommendedNewss.add(news);
            recommendedNewsIds.add(news.getId());
        }
    }

    // 计算要添加的健康生活类别新闻的数量
    private int calculateHealthCategoryNewsToAdd(boolean keywordNewsRecommended, int totalRecommendedNewsCount, List<News> filteredNewss, Set<String> allIllnessTypes, int recommendedDiseaseNewsCount) {
        // 在方法内部计算历史相关新闻和疾病相关新闻的数量
        int historyNewsCount = filteredNewss.size();

        // 健康生活类别新闻的数量
        int healthCategoryNewsCount;

        // 根据历史相关新闻和疾病相关新闻的总数计算健康生活类别新闻的数量
        if (keywordNewsRecommended) {
            // 如果有关键字新闻，占比为 40%
            healthCategoryNewsCount = (int) ((historyNewsCount + recommendedDiseaseNewsCount) * 0.4);
        } else {
            // 如果没有关键字新闻，占比为 50%
            healthCategoryNewsCount = (int) ((recommendedDiseaseNewsCount) * 0.5 );
        }

        // 输出调试信息
        System.out.println("keywordNewsRecommended: " + keywordNewsRecommended);
        System.out.println("History news count: " + historyNewsCount);
        System.out.println("Disease news count: " + recommendedDiseaseNewsCount);
        System.out.println("Health category news to add: " + healthCategoryNewsCount);

        // 确保至少添加一篇健康生活类别的新闻
        return Math.max(1, healthCategoryNewsCount);
    }

    // 计算新闻的综合权重
    private double calculateWeight(News news) {
        // 获取点赞量、浏览量和发布时间
        Long likes = news.getLikes() != null ? news.getLikes() : 0L;
        Long views = news.getClick() != null ? news.getClick() : 0L;
        String timeString = news.getTime();

        // 将时间字符串解析为日期对象
        Date publishDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            publishDate = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 计算时间权重
        double timeWeight = 0.5; // 初始化为0.5
        if (publishDate != null) {
            long currentTime = System.currentTimeMillis();
            long newsTime = publishDate.getTime();
            long timeDiff = currentTime - newsTime;
            if (timeDiff > 0) {
                double hours = timeDiff / (1000.0 * 60 * 60); // 转换为小时
                timeWeight = 0.5 * (1 / (1 + Math.exp(-hours))); // 使用sigmoid函数计算时间权重，确保最新的新闻权重接近0.5
            }
        }

        // 计算点赞量和浏览量的权重
        double likeWeight = likes * 0.3;
        double viewWeight = views * 0.2;

        // 计算综合权重
        return timeWeight + likeWeight + viewWeight;
    }


    // 根据用户历史记录关键字筛选新闻
    private List<News> filterNewsByHistoryKeywords(List<News> allNewsList, List<String> userHistoryKeywords) {

        // 进行关键字筛选，保留包含用户历史记录关键字的新闻
        List<News> filteredNewsList = new ArrayList<>();
        for (News news : allNewsList) {
            String keywords = news.getKeywords();
            if (keywords != null && !keywords.isEmpty()) {
                // 分割关键字字符串
                String[] keywordArray = keywords.split(",");
                for (String keyword : keywordArray) {
                    // 去除空格并转为小写
                    String cleanedKeyword = keyword.trim().toLowerCase();
                    if (userHistoryKeywords.contains(cleanedKeyword)) {
                        // 如果新闻的关键词与用户的关键词列表中的任何一个匹配，则将该新闻添加到筛选后的列表中
                        filteredNewsList.add(news);
                        System.out.println("关键词相关新闻: " + news.getTitle()); // 输出标题
                        break; // 跳出内层循环，避免重复添加同一新闻
                    }
                }
            }
        }

        return filteredNewsList;
    }

    /**
     * 根据用户ID获取老人信息
     * @param userId 用户ID
     * @return 老人信息列表
     */
    @GetMapping("/old")
    public List<Map<String, Object>> getOldinfoByUserId(@RequestParam("userId") Long userId) {
        try {
            System.out.println("Received userId: " + userId); // 输出接收到的 userId 值
            // 根据用户ID获取家庭信息
            Familyinfo familyinfo = familyinfoService.getFamilyInfoByUserId(userId);
            // 获取用户对应的老人信息的 ID 列表
            List<Long> oldInfoIds = familyinfo.getOldInfoIds();
            System.out.println("oldInfoIds: " + oldInfoIds);

            // 根据老人信息的 ID 列表查询老人信息
            List<Map<String, Object>> oldInfoList = new ArrayList<>();
            for (Long oldInfoId : oldInfoIds) {
                // 获取完整的老人信息对象
                // 在控制器或其他地方调用 findById 方法获取老人信息对象
                Oldinfo oldInfo = oldInfoService.findById(oldInfoId);
                // 构建老人信息的 Map 对象
                Map<String, Object> oldInfoMap = new HashMap<>();
                oldInfoMap.put("id", oldInfo.getId());
                oldInfoMap.put("name", oldInfo.getName());
                oldInfoMap.put("image", oldInfo.getPicture());
                oldInfoMap.put("age", oldInfo.getEage());
                oldInfoMap.put("sex", oldInfo.getEsex());
                oldInfoMap.put("birthdate", oldInfo.getBirthdate());
                oldInfoMap.put("phone", oldInfo.getPhone());
                oldInfoMap.put("street", oldInfo.getEstreet());
                oldInfoMap.put("address", oldInfo.getEaddress());
                oldInfoMap.put("health", oldInfo.getEhealth());
                oldInfoMap.put("body", oldInfo.getBody());

                // 将老人信息添加到列表中
                oldInfoList.add(oldInfoMap);
            }

//            System.out.println("oldInfoList: " + oldInfoList);
            return oldInfoList;
        } catch (Exception e) {
            // 如果发生异常，返回空列表
            System.err.println("Error while fetching oldinfos: " + e.getMessage());
            return Collections.emptyList();
        }
    }


}
