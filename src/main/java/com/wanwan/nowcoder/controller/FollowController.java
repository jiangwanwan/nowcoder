package com.wanwan.nowcoder.controller;

import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventProducer;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.model.Question;
import com.wanwan.nowcoder.model.User;
import com.wanwan.nowcoder.model.ViewObject;
import com.wanwan.nowcoder.service.CommentService;
import com.wanwan.nowcoder.service.FollowService;
import com.wanwan.nowcoder.service.QuestionService;
import com.wanwan.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wanwan Jiang
 * @Description: 关注控制器
 * @Date: Created in 8:34 2017/10/17
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    /**
     * 关注用户
     * @param userId
     * @return
     */
    @RequestMapping(value = {"/followUser"}, method = {RequestMethod.POST})
    public String followUser(@RequestParam("userId") int userId){
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }

            boolean ret = followService.follow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);

            /**
             * 异步：关注用户后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                                    .setActorId(hostHolder.getUser().getId())
                                    .setEntityId(userId)
                                    .setEntityType(EntityType.ENTITY_USER)
                                    .setEntityOwnerId(userId));

            return "redirect:/user/" + userId;
        }catch (Exception e){
            logger.error("关注用户失败:" + e.getMessage());
        }
        return "reglogin";
    }


    /**
     * 取消关注用户
     * @param userId
     * @return
     */
    @RequestMapping(value = {"/unfollowUser"}, method = {RequestMethod.POST})
    public String unfollowUser(@RequestParam("userId") int userId){
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }

            boolean ret = followService.unfollow(hostHolder.getUser().getId(), userId, EntityType.ENTITY_USER);

            /**
             * 异步：取消关注用户后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(userId)
                    .setEntityType(EntityType.ENTITY_USER)
                    .setEntityOwnerId(userId));

            return "redirect:/user/" + userId;
        }catch (Exception e){
            logger.error("取消关注用户失败:" + e.getMessage());
        }
        return "reglogin";
    }

    /**
     * 关注问题
     * @param model
     * @param questionId
     * @return
     */
    @RequestMapping(value = {"/followQuestion"}, method = {RequestMethod.POST})
    public String followQuestion(Model model,
                                 @RequestParam("questionId") int questionId){
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }

            Question question = questionService.selectById(questionId);
            if (question == null){
                logger.error("问题不存在");
            }

            boolean ret = followService.follow(hostHolder.getUser().getId(), questionId, EntityType.ENTITY_QUESTION);

            /**
             * 异步：关注问题后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(questionId)
                    .setEntityType(EntityType.ENTITY_QUESTION)
                    .setEntityOwnerId(question.getUserId()));

            return "redirect:/question/" + questionId;
        }catch (Exception e){
            logger.error("关注用户失败:" + e.getMessage());
        }
        return "reglogin";
    }

    /**
     * 取消关注问题
     * @param questionId
     * @return
     */
    @RequestMapping(value = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        try {
            if (hostHolder.getUser() == null){
                return "reglogin";
            }

            Question question = questionService.selectById(questionId);
            if (question == null){
                logger.error("问题不存在");
            }

            boolean ret = followService.unfollow(hostHolder.getUser().getId(), questionId, EntityType.ENTITY_QUESTION);

            /**
             * 异步：取消关注问题后，发送私信
             */
            eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(questionId)
                    .setEntityType(EntityType.ENTITY_QUESTION)
                    .setEntityOwnerId(question.getUserId()));

            return "redirect:/question/" + questionId;
        }catch (Exception e){
            logger.error("关注用户失败:" + e.getMessage());
        }
        return "reglogin";
    }

    /**
     * 查看用户userId的所有粉丝
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model,
                            @PathVariable("uid") int userId){
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null){
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        }else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    /**
     * 查看用户userId的所有关注者
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds){
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds){
            User user = userService.getUser(uid);
            if (user == null){
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
