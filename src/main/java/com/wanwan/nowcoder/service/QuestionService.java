package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.dao.QuestionDao;
import com.wanwan.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 21:41 2017/10/9
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    /**
     * 添加问题
     * @param question
     * @return
     */
    public int addQuestion(Question question){

        //html标签过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDao.addQuestion(question)>0 ? question.getId():0;
    }

    /**
     * 查找指定用户的最新的几条问题信息
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    /**
     * 根据问题id获取对应的问题
     * @param id
     * @return
     */
    public Question selectById(int id){
        return questionDao.selectById(id);
    }

    /**
     * 根据id，更新对应问题的评论数量
     * @param id
     * @param count
     */
    public void updateCommentCount(int id, int count){
        questionDao.updateCommentCount(id, count);
    }
}
