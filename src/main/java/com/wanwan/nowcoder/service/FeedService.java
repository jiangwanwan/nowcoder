package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.dao.FeedDao;
import com.wanwan.nowcoder.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 9:38 2017/10/18
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class FeedService {
    @Autowired
    FeedDao feedDao;

    public boolean addFeed(Feed feed){
        feedDao.addFeed(feed);
        return feed.getId()>0;
    }

    public Feed getFeedById(int id){
        return feedDao.getFeedById(id);
    }

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDao.selectUserFeeds(maxId, userIds, count);
    }
}
