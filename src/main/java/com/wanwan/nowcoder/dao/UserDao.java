package com.wanwan.nowcoder.dao;

import com.wanwan.nowcoder.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 19:25 2017/10/9
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 添加用户
     * @param user
     * @return
     */
    @Insert(value = "insert into " +TABLE_NAME+ " ( "+ INSERT_FIELDS +" ) values (#{name}, #{password}, #{salt}, #{headUrl})")
    int addUser(User user);

    /**
     * 根据id删除对应用户
     * @param id
     */
    @Delete(value = "delete from "+TABLE_NAME+" where id = #{id}")
    void deleteById(int id);

    /**
     * 根据id修改对应用户密码
     * @param user
     */
    @Update(value = "update "+TABLE_NAME+" set password = #{password} where id = #{id}")
    void updatePassword(User user);

    /**
     * 根据id查询对应用户
     * @param id
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id = #{id}")
    User selectById(int id);

    /**
     * 根据name（用户名）查询对应用户
     * 作用：用于注册时，用户查重
     * @param name
     * @return
     */
    @Select(value = "select "+SELECT_FIELDS+" from "+TABLE_NAME+" where name = #{name}")
    User selectByName(String name);
}
