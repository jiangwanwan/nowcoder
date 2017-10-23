package com.wanwan.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.wanwan.nowcoder.async.EventHandler;
import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.Feed;
import com.wanwan.nowcoder.model.Message;
import com.wanwan.nowcoder.model.Question;
import com.wanwan.nowcoder.model.User;
import com.wanwan.nowcoder.service.FeedService;
import com.wanwan.nowcoder.service.FollowService;
import com.wanwan.nowcoder.service.MessageService;
import com.wanwan.nowcoder.service.QuestionService;
import com.wanwan.nowcoder.service.UserService;
import com.wanwan.nowcoder.util.JedisAdapter;
import com.wanwan.nowcoder.util.RedisKeyUtil;
import com.wanwan.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wanwan Jiang
 * @Description: 新鲜事事件处理器:发生评论问题或关注问题事件时，异步地将新鲜事添加到数据库中
 * @Date: Created in 14:35 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Component
public class FeedHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    // 构造核心数据
    private String buildFeedData(EventModel model){
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null){
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT ||(model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.selectById(model.getEntityId());
            if (question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null){
            return;
        }

        feedService.addFeed(feed);

        // 给事件的粉丝推
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        for (int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
