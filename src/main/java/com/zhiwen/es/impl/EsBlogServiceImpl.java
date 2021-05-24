package com.zhiwen.es.impl;

import com.zhiwen.constant.RedisConstant;
import com.zhiwen.entity.BlogTagExample;
import com.zhiwen.entity.User;
import com.zhiwen.es.EsBlogDo;
import com.zhiwen.es.EsBlogRepository;
import com.zhiwen.es.EsBlogService;
import com.zhiwen.mapper.BlogTagMapper;
import com.zhiwen.mapper.UserMapper;
import com.zhiwen.util.RedisValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * es服务实现
 *

 * @date : 2020/1/19 16:35
 */
@Slf4j
@Service
public class EsBlogServiceImpl implements EsBlogService {

    @Autowired
    private EsBlogRepository esBlogRepository;

    @Autowired
    private RedisValueUtil redisValueUtil;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<EsBlogDo> search(String keyword, Integer pageNum, Integer pageSize,
                                 Integer userId) {
        //参数不合法调整
        if (pageNum < 0) {
            pageNum = 0;
        }
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.matchQuery("delFlag", false))
                .must(QueryBuilders.matchQuery("published", true));
        if (StringUtils.isNotBlank(keyword)) {
            //组装模糊查询条件
            query.must(QueryBuilders.fuzzyQuery("blogTitle", keyword.toLowerCase()));
        }
        Page<EsBlogDo> page = esBlogRepository.search(query, PageRequest.of(pageNum, pageSize,
                new Sort(Sort.Direction.DESC, "createTime")));
        for (EsBlogDo esBlogDo : page) {
            BlogTagExample blogTagExample = new BlogTagExample();
            blogTagExample.createCriteria().andBlogIdEqualTo(esBlogDo.getBlogId());
            esBlogDo.setTags(blogTagMapper.selectByExample(blogTagExample));
            User user = userMapper.selectByPrimaryKey(esBlogDo.getUserId());
            esBlogDo.setNickname(user.getNickname());
            esBlogDo.setHeadPortrait(user.getHeadPortrait());
            //从缓存中取博客评论数和浏览数
            if (redisValueUtil.hExists(RedisConstant.COMMENT_KEY,
                    esBlogDo.getBlogId().toString())) {
                esBlogDo.setBlogViews((Integer) redisValueUtil.hGet(RedisConstant.VIEW_KEY,
                        esBlogDo.getBlogId().toString()));
            }
            if (redisValueUtil.hExists(RedisConstant.COMMENT_KEY,
                    esBlogDo.getBlogId().toString())) {
                esBlogDo.setBlogComments((Integer) redisValueUtil.hGet(RedisConstant.COMMENT_KEY,
                        esBlogDo.getBlogId().toString()));
            }
            if (redisValueUtil.hExists(RedisConstant.LIKE_KEY,
                    esBlogDo.getBlogId().toString())) {
                HashSet<Integer> set =
                        (HashSet<Integer>) redisValueUtil.hGet(RedisConstant.LIKE_KEY,
                                esBlogDo.getBlogId().toString());
                esBlogDo.setBlogLikes(set.size());
                if (userId == null) {
                    esBlogDo.setFlag(false);
                } else {
                    esBlogDo.setFlag(set.contains(userId));
                }
                log.info("更新博客编号[{}]的点赞数为redis最新点赞数据：{}", esBlogDo.getBlogId(),
                        set.size());
            }
        }
        return page;
    }
}
