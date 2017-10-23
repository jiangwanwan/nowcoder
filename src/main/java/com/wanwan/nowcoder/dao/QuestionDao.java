package com.wanwan.nowcoder.dao;

import com.wanwan.nowcoder.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 20:04 2017/10/9
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
@Mapper
public interface QuestionDao {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加问题
     * @param question
     * @return
     */
    @Insert(value = "insert into "+TABLE_NAME+" ("+INSERT_FIELDS+") values (#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})")
    int addQuestion(Question question);

    /**
     * 查找指定用户的最新的几条问题信息(使用xml配置)
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    /**
     * 根据问题id获取对应的问题
     * @param id
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id = #{id}")
    Question selectById(int id);


    @Update(value = "update "+TABLE_NAME+" set comment_count = #{count} where id = #{id}")
    void updateCommentCount(@Param("id") int id,
                            @Param("count") int count);
}
