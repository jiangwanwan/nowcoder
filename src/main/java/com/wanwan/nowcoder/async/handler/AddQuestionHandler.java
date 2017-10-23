package com.wanwan.nowcoder.async.handler;

import com.wanwan.nowcoder.async.EventHandler;
import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description: 添加问题事件处理器：当添加问题成功时，异步对问题进行索引
 * @Date: Created in 20:11 2017/10/22
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Component
public class AddQuestionHandler implements EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(), model.getExt("title"), model.getExt("content"));
        }catch (Exception e){
            logger.error("增加问题索引失败：" + e.getMessage());
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
