package com.wanwan.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.wanwan.nowcoder.util.JedisAdapter;
import com.wanwan.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wanwan Jiang
 * @Description: 事件消费者：用于维护event和handler之间的关系
 * @Date: Created in 14:03 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

        // 获取EventHandler接口所有的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();       //获取handler所关注的事件

                for (EventType eventType : eventTypes){
                    if (!config.containsKey(eventType)){
                        config.put(eventType, new ArrayList<>());
                    }

                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        /**
         * 创建一个线程，一直从事件队列中取事件
         */
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String event : events){
                        if (event.equals(key)){
                            continue;
                        }

                        EventModel eventModel = JSONObject.parseObject(event, EventModel.class);        //反序列化

                        if (!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件" );
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
