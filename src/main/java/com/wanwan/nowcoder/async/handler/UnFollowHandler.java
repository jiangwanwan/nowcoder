package com.wanwan.nowcoder.async.handler;

import com.wanwan.nowcoder.async.EventHandler;
import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.Message;
import com.wanwan.nowcoder.model.User;
import com.wanwan.nowcoder.service.MessageService;
import com.wanwan.nowcoder.service.UserService;
import com.wanwan.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description: 取消关注事件处理器：收到取消关注事件时，给对应用户发送站内信
 * @Date: Created in 10:41 2017/10/17
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Component
public class UnFollowHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();

        message.setFromId(WendaUtil.SYSTEM_USERID); //发送者：系统
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());

        User user = userService.getUser(model.getActorId());
        if (model.getEntityType() == EntityType.ENTITY_QUESTION){
            message.setContent("用户: " + user.getName() + "取消关注你的问题, http://127.0.0.1:8080/question/" + model.getEntityId());
        }else if (model.getEntityType() == EntityType.ENTITY_USER){
            message.setContent("用户: " + user.getName() + "取消关注你, http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.UNFOLLOW);
    }
}
