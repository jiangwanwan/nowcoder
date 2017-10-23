package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.dao.CommentDao;
import com.wanwan.nowcoder.model.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 15:11 2017/10/14
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment){
        //HTML标签过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //敏感词过滤
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment)>0 ? comment.getId():0;
    }

    /**
     * 根据id删除对应的评论
     * @param commentId
     * @return
     */
    public boolean deleteComment(int commentId){
        return commentDao.updateStatus(commentId, 1) > 0;
    }

    /**
     * 根据实体类型获取对应实体的评论
     * @param entityId
     * @param entityType
     * @return
     */
    public List<Comment> selectCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId, entityType);
    }

    /**
     * 根据实体类型获取对应实体的评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId, entityType);
    }


    public Comment getComment(int commentId){
        return commentDao.getComment(commentId);
    }
}
