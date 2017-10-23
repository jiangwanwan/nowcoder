package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.util.JedisAdapter;
import com.wanwan.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: Wanwan Jiang
 * @Description: 关注服务
 * @Date: Created in 20:21 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class FollowService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**\
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     *
     * A关注B：
     * 1、A的关注列表中加入B
     * 2、B的粉丝列表中加入A
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public boolean follow(int userId, int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        // 在实体的粉丝中增加当前用户
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        // 在当前用户的关注对象中增加该实体
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(0) > 0;
    }

    /**\
     * 取消关注（userId不关注实体entityId）
     *
     * A不关注B：
     * 1、A的关注列表中删除B
     * 2、B的粉丝列表中删除A
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public boolean unfollow(int userId, int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        // 在实体的粉丝中删除当前用户
        tx.zrem(followerKey, String.valueOf(userId));
        // 在当前用户的关注对象中删除该实体
        tx.zrem(followeeKey, String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);

        return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(0) > 0;
    }

    /**
     * 将Set<String>类型 --》 List<Integer>类型
     * @param idset
     * @return
     */
    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for (String str : idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    /**
     * 获取实体（entityId+entityType）的所有粉丝列表
     * @param entityType
     * @param entityId
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey, offset, count));
    }

    /**
     * 获取用户（userId）的所有关注对象列表
     * @param userId
     * @param entityType
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int userId, int entityType, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    /**
     * 获取实体（entityId+entityType）的所有粉丝数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    /**
     * 获取用户（userId）所关注这一类实体的数量
     * @param userId
     * @param entityType
     * @return
     */
    public long getFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    /**
     * 判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
