package com.wanwan.nowcoder.dao;

import com.wanwan.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 9:32 2017/10/15
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加站内信
     * @param message
     * @return
     */
    @Insert(value = "insert into " +TABLE_NAME+ " ( "+ INSERT_FIELDS +" ) values (#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})")
    int addMessage(Message message);

    /**
     * 根据conversationId获取对应的消息列表详情
     * @param conversationId
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where conversation_id = #{conversationId} order by created_date desc")
    List<Message> getConversationDetail(@Param("conversationId") String conversationId);


    /**
     * 根据userId获取与该用户私信的所有消息列表
     * @param userId
     * @return
     */
    @Select(value = "select "+INSERT_FIELDS+", count(id) as id from (select * from "+TABLE_NAME+" where from_id = #{userId} or to_id = #{userId} order by created_date desc) tt group by conversation_id order by created_date desc")
    List<Message> getConversationList(@Param("userId") int userId);

}
