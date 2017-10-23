package com.wanwan.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.wanwan.nowcoder.util.JedisAdapter;
import com.wanwan.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Wanwan Jiang
 * @Description: 事件生产者：用于生产事件
 * @Date: Created in 13:35 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Component
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            /*方式一：队列形式
            BlockingQueue<EventModel> q = new ArrayBlockingQueue<EventModel>();*/

            // 方式二：redis(序列化与反序列化)
            String json = JSONObject.toJSONString(eventModel); //序列化
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
