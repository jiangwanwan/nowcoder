package com.wanwan.nowcoder.controller;

import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.model.Message;
import com.wanwan.nowcoder.model.User;
import com.wanwan.nowcoder.model.ViewObject;
import com.wanwan.nowcoder.service.MessageService;
import com.wanwan.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description: 站内信控制器
 * @Date: Created in 9:48 2017/10/15
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    /**
     * 发送私信
     * @param model
     * @param toName
     * @param content
     * @return
     */
    @RequestMapping(value = {"/msg/addMessage"}, method = {RequestMethod.POST})
    public String addMessage(Model model,
                             @RequestParam("toName") String toName,
                             @RequestParam("content") String content){

        try {
            if (hostHolder.getUser() == null){
                return "/reglogin";
            }else {
                User toUser = userService.selectByName(toName);
                if (toUser == null){
                    System.out.println("用户不存在");
                }

                Message message = new Message();
                message.setFromId(hostHolder.getUser().getId());
                message.setToId(toUser.getId());
                message.setContent(content);
                message.setCreatedDate(new Date());

                messageService.addMessage(message);
            }
        }catch (Exception e){
            logger.error("发送站内信失败：" + e.getMessage());
            return "redirect:";
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/msg/list", method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try {
            if (hostHolder.getUser() == null){
                return "/reglogin";
            }else {
                int localUserId = hostHolder.getUser().getId();
                List<Message> conversationList = messageService.getConversationList(localUserId);
                List<ViewObject> conversations = new ArrayList<>();

                for (Message message : conversationList){
                    ViewObject vo = new ViewObject();
                    vo.set("message", message);
                    int targetId = (message.getFromId() == localUserId ? message.getToId():message.getFromId());
                    vo.set("user", userService.getUser(targetId));

                    conversations.add(vo);
                }

                model.addAttribute("conversations", conversations);
            }
        }catch (Exception e){
            logger.error("获取消息列表失败：" + e.getMessage());
        }
        return "letter";
    }

    /**
     * 站内信详情
     * @param model
     * @param conversationId
     * @return
     */
    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId){

        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId);
            List<ViewObject> messages = new ArrayList<>();

            for (Message message : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }

            model.addAttribute("messages", messages);
        }catch (Exception e){
            logger.error("获取详情失败：" + e.getMessage());
        }
        return "letterDetail";
    }
}
