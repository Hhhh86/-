package com.zhiwen.service.impl;

import com.github.pagehelper.PageHelper;
import com.zhiwen.entity.Reply;
import com.zhiwen.entity.User;
import com.zhiwen.mapper.ReplyExtendMapper;
import com.zhiwen.mapper.ReplyMapper;
import com.zhiwen.pojo.ReplyExtend;
import com.zhiwen.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 回复服务实现类
 *

 * @date : 2020/12/11 17:56
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyExtendMapper replyExtendMapper;

    @Autowired
    private ReplyMapper replyMapper;

    /**
     * 功能描述: 查询某评论下的回复
     *
     * @param commentId 1
     * @param pageNum   2
     * @param pageSize  3
     * @return java.util.List<com.zhiwen.pojo.ReplyExtend>
     *
     * @date 2020/12/11 17:59
     **/
    @Override
    public List<ReplyExtend> getReplyList(Integer commentId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "reply_time");
        List<ReplyExtend> replyExtendList = replyExtendMapper.getReplys(commentId);
        return replyExtendList;
    }

    /**
     * 功能描述: 新增回复信息
     *
     * @param commentId 1
     * @param blogId 2
     * @param content 3
     * @param user 4
     * @param repliedUserId 5
     * @param replyType 6
     *
     * @date 2020/1/19 11:32
     **/
    @Override
    public void addReplys(Integer commentId, Integer blogId, String content, User user, Integer repliedUserId,
                          String replyType) {
        Reply reply = new Reply();
        reply.setCommentId(commentId);
        reply.setReplyContent(content);
        reply.setUserId(user.getUserId());
        Date date = new Date();
        reply.setRepliedUserId(repliedUserId);
        reply.setReplyTime(date);
        reply.setCreateTime(date);
        reply.setUpdateTime(date);
        reply.setDelFlag(false);
        replyMapper.insertSelective(reply);
    }
}
