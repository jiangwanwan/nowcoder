package com.wanwan.nowcoder.controller;

import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventProducer;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.Comment;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.model.Question;
import com.wanwan.nowcoder.model.ViewObject;
import com.wanwan.nowcoder.service.CommentService;
import com.wanwan.nowcoder.service.FollowService;
import com.wanwan.nowcoder.service.LikeService;
import com.wanwan.nowcoder.service.QuestionService;
import com.wanwan.nowcoder.service.UserService;
import com.wanwan.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description: 问题控制器
 * @Date: Created in 21:24 2017/10/12
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;
    /**
     * 发起提问
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = {"/question/add"}, method = {RequestMethod.POST})
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try {

            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);

            if (hostHolder.getUser() == null){      //用户没有登陆
                //question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return "/reglogin";
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }

            if (questionService.addQuestion(question) > 0){ //发起问题成功
                //return WendaUtil.getJSONString(0);

                /**
                 * 发起问题成功，异步对问题进行索引
                 */
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                                        .setActorId(hostHolder.getUser().getId())
                                        .setEntityId(question.getId())
                                        .setExt("title", question.getTitle())
                                        .setExt("content", question.getContent()));
                return "redirect:/";
            }
        }catch (Exception e){
            logger.error("发起问题失败：" + e.getMessage());
        }
        //return WendaUtil.getJSONString(1, "失败");
        return "redirect:/";
    }

    /**
     * 查看具体问题
     * @param qid
     * @return
     */
    @RequestMapping(value = {"/question/{qid}"}, method = {RequestMethod.GET})
    public String questionDetail(Model model,
                                 @PathVariable("qid") int qid){
        Question question = questionService.selectById(qid);
        List<Comment> commentList = commentService.selectCommentByEntity(qid, EntityType.ENTITY_QUESTION);

        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null){
                vo.set("liked", 0);
            }else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));

            comments.add(vo);
        }

        if (hostHolder.getUser() != null){
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        }else {
            model.addAttribute("followed", false);
        }

        model.addAttribute("question", question);
        model.addAttribute("comments", comments);
        model.addAttribute("user", userService.getUser(question.getUserId()));


        return "detail";
    }
}
