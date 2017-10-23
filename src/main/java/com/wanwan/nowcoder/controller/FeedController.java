package com.wanwan.nowcoder.controller;

import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.Feed;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.service.FeedService;
import com.wanwan.nowcoder.service.FollowService;
import com.wanwan.nowcoder.util.JedisAdapter;
import com.wanwan.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description: 新鲜事控制器
 * @Date: Created in 10:46 2017/10/18
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 采用拉的模式，直接从数据库中获取新鲜事
     * @param model
     * @return
     */
    @RequestMapping(value = {"/pullfeeds"})
    private String getPullFeeds(Model model){

        int localUserId = hostHolder.getUser() == null ? 0:hostHolder.getUser().getId();

        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0){
            //找到userId所有关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        //获取所有关注的人的新鲜事
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);

        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    /**
     * 采用推的模式：从redis中获取新鲜事的id，然后去数据库中查
     * @param model
     * @return
     */
    @RequestMapping(value = {"/pushfeeds"})
    private String getPushFeeds(Model model){

        int localUserId = hostHolder.getUser() == null ? 0:hostHolder.getUser().getId();

        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);

        List<Feed> feeds = new ArrayList<>();

        for (String feedId : feedIds){
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if (feed == null){
                continue;
            }
            feeds.add(feed);
        }

        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
