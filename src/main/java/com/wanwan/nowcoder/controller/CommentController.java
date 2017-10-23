package com.wanwan.nowcoder.controller;

import com.alibaba.fastjson.JSONObject;
import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventProducer;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.Comment;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.model.Question;
import com.wanwan.nowcoder.service.CommentService;
import com.wanwan.nowcoder.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: Wanwan Jiang
 * @Description: 评论控制器
 * @Date: Created in 15:23 2017/10/14
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    /**
     * 给对应的问题添加评论(注：更新对应问题的评论数量)
     * @param questionId
     * @param content
     * @return
     */
    @RequestMapping(value = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){

        try {
            Comment comment = new Comment();
            comment.setContent(content);

            if (hostHolder.getUser() != null){
                comment.setUserId(hostHolder.getUser().getId());
            }else {
                return "reglogin";
            }

            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());

            commentService.addComment(comment);

            /**
             * 异步：评论问题后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(questionId)
                    .setEntityType(EntityType.ENTITY_QUESTION)
                    .setEntityOwnerId(questionService.selectById(questionId).getUserId()));

            //更新对应问题的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);

        }catch (Exception e){
            logger.error("评论问题失败：" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }

}
