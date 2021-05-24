package com.zhiwen.task;

import com.zhiwen.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @date : 2020/1/7 08:41
 */
@Component
@Slf4j
public class MyTask {
    public final static long ONE_MINUTE = 60 * 1000;

    @Autowired
    private BlogService blogService;

    /**
     * 功能描述: 同步博客信息至es中：同步周期（3分钟）
     *
     *
     * @date 2020/1/19 10:20
     **/
    @Scheduled(fixedDelay = 3 * ONE_MINUTE)
    public void syncBlogToEs() {
        log.info("执行博客信息同步至ES中>>>>>>start");
        long start = System.currentTimeMillis();
        Integer count = blogService.syncBlogToEs();
        long end = System.currentTimeMillis();
        log.info("执行博客信息同步至ES中>>>>>>end，共更新数据：{}条，执行时间：{}s", count, ((float) (end - start)) / 1000);
    }

    /**
     * 功能描述: 同步博客浏览量、评论数和点赞数信息（from Redis）：同步周期每小时
     *
     *
     * @date 2020/1/19 10:20
     **/
    @Scheduled(fixedDelay = 60 * ONE_MINUTE)
    public void syncBlogInfoFromRedis() {
        log.info("执行同步博客浏览量、评论数和点赞数任务（from Redis）>>>>>>start");
        long start = System.currentTimeMillis();
        blogService.syncBlogInfoFromRedis();
        long end = System.currentTimeMillis();
        log.info("执行同步博客浏览量、评论数和点赞数任务（from Redis）>>>>>>end，执行时间：{}s",
                ((float) (end - start)) / 1000);
    }

}
