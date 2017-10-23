package com.wanwan.nowcoder.controller;

import com.wanwan.nowcoder.async.EventModel;
import com.wanwan.nowcoder.async.EventProducer;
import com.wanwan.nowcoder.async.EventType;
import com.wanwan.nowcoder.model.Comment;
import com.wanwan.nowcoder.model.EntityType;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.model.Question;
import com.wanwan.nowcoder.model.ViewObject;
import com.wanwan.nowcoder.service.CommentService;
import com.wanwan.nowcoder.service.FollowService;
import com.wanwan.nowcoder.service.QuestionService;
import com.wanwan.nowcoder.service.SearchService;
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
 * @Description: 搜索控制器
 * @Date: Created in 15:23 2017/10/14
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(value = {"/search"}, method = {RequestMethod.POST})
    public String search(Model model,
                         @RequestParam("keyword") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count){
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count,
                    "<font>", "</font>");
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                Question q = questionService.selectById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUser(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);

        }catch (Exception e){
            logger.error("搜索问题失败：" + e.getMessage());
        }
        return "result";
    }

}
