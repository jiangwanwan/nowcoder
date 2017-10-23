package com.wanwan.nowcoder.dao;

import com.wanwan.nowcoder.model.Comment;
import com.wanwan.nowcoder.model.Feed;
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
public interface FeedDao {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " created_date, user_id, data, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加新鲜事
     * @param feed
     * @return
     */
    @Insert(value = "insert into "+TABLE_NAME+" ("+INSERT_FIELDS+") values (#{createdDate}, #{userId}, #{data}, #{type})")
    int addFeed(Feed feed);

    /**
     * （采用推的模式）
     * @param id
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id = #{id}")
    Feed getFeedById(int id);

    /**
     * 动态sql：查询userId所关注的用户们的新鲜事（采用拉的模式）
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
