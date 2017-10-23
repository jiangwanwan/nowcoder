package com.wanwan.nowcoder.dao;

import com.wanwan.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 16:20 2017/10/11
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Mapper
public interface LoginTicketDao {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 增加t票
     * @param ticket
     * @return
     */
    @Insert(value = "insert into "+TABLE_NAME+" ("+INSERT_FIELDS+") values (#{userId}, #{ticket}, #{expired}, #{status})")
    int addTicket(LoginTicket ticket);

    /**
     * 用户登出，修改用户状态
     * @param ticket
     * @param status
     */
    @Update(value = "update "+TABLE_NAME+" set status = #{status} where ticket = #{ticket}")
    void updateStatus(@Param("ticket") String ticket,
                      @Param("status") int status);

    /**
     * 根据ticket查询对应的t票
     * @param ticket
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where ticket = #{ticket}")
    LoginTicket selectByTicket(String ticket);
}
