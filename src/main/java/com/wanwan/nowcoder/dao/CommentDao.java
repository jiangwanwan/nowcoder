package com.wanwan.nowcoder.dao;

import com.wanwan.nowcoder.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 14:53 2017/10/14
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加评论
     * @param comment
     * @return
     */
    @Insert(value = "insert into "+TABLE_NAME+" ("+INSERT_FIELDS+") values (#{content}, #{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status})")
    int addComment(Comment comment);

    /**
     * 删除评论
     * @param id
     * @param status
     * @return
     */
    @Update(value = "update "+TABLE_NAME+" set status = #{status} where id = #{id}")
    int updateStatus(@Param("id") int id,
                      @Param("status") int status);

    /**
     * 根据实体类型查询对应的评论
     * @param entityId
     * @param entityType
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc")
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);

    /**
     * 根据实体类型获取对应实体的评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    @Select(value = "select count(id) from "+TABLE_NAME+" where entity_id = #{entityId} and entity_type = #{entityType}")
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);

    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id = #{commentId}")
    Comment getComment(int commentId);
}
