package com.wanwan.nowcoder.controller;



import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventProducer;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.Comment;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.service.CommentService;
import com.wanwan.nowcoder.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Wanwan Jiang
 * @Description: 点赞控制器(使用redis)
 * @Date: Created in 8:46 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;

    /**
     * 对评论点赞
     * @param commentId
     * @return
     */
    @RequestMapping(value = {"/like"}, method = {RequestMethod.POST})
    public String like(Model model,
                       @RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null){
            return "reglogin";
        }

        Comment comment = commentService.getComment(commentId);

        /**
         * 异步：点赞后发送私信
         */
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        model.addAttribute("likeCount", likeCount);

        int qid = comment.getEntityId();
        return "redirect:/question/" + qid;
    }

    /**
     * 对评论点踩
     * @param commentId
     * @return
     */
    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.POST})
    public String dislike(Model model,
                          @RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null){
            return "reglogin:";
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        model.addAttribute("likeCount", likeCount);

        int qid = commentService.getComment(commentId).getEntityId();
        return "redirect:/question/" + qid;
    }

}
