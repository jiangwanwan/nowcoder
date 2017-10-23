package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.dao.MessageDao;
import com.wanwan.nowcoder.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 9:45 2017/10/15
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message)>0 ? message.getId():0;
    }

    public List<Message> getConversationDetail(String conversationId){
        return messageDao.getConversationDetail(conversationId);
    }

    public List<Message> getConversationList(int userId){
        return messageDao.getConversationList(userId);
    }
}
