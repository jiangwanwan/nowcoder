package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.util.JedisAdapter;
import com.wanwan.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Wanwan Jiang
 * @Description: 赞踩业务
 * @Date: Created in 8:50 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 获取当前有多少人点赞
     * @param entityType
     * @param entityId
     * @return
     */
    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    /**
     * 获取用户当前的点赞状态
     * 如果返回1：用户已点赞
     * 如果返回-1：用户已点踩
     * 如果返回0：用户未点赞且未点踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);

        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        if (jedisAdapter.sismember(disLikeKey, String.valueOf(userId))){
            return -1;
        }

        return 0;
    }

    /**
     * 点赞
     * @param userId
     * @param entityId
     * @param entityType
     * @return 共有多少用户点赞
     */
    public long like(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    /**
     * 点踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return 共有多少用户点赞
     */
    public long disLike(int userId, int entityType, int entityId){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }


}
