package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.dao.LoginTicketDao;
import com.wanwan.nowcoder.dao.UserDao;
import com.wanwan.nowcoder.model.LoginTicket;
import com.wanwan.nowcoder.model.User;
import com.wanwan.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 21:40 2017/10/9
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketService loginTicketService;

    @Autowired
    LoginTicketDao loginTicketDao;

    /**
     * 用户注册
     * @param username
     * @param password
     * @return 注册提示信息map。如果注册失败，返回的map为注册失败信息。如果注册成功，返回的map为t票。
     */
    public Map<String, String> register(String username, String password){
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空！");
            return map;
        }

        User user = userDao.selectByName(username);
        if (user!=null){
            map.put("msg","该用户名已经被注册！");
            return map;
        }

        /**
         * 开始用户注册
         */
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDao.addUser(user);

        /**
         * 注册成功，下发t票
         */
        String ticket = loginTicketService.addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return 登陆提示信息map。如果登陆失败，返回的map为登陆失败信息。如果登陆成功，返回的map为t票。
     */
    public Map<String, String> login(String username, String password){
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空！");
            return map;
        }

        User user = userDao.selectByName(username);
        if (user==null){
            map.put("msg","用户名不存在！");
            return map;
        }

        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg", "密码错误！");
            return map;
        }

        /**
         * 用户登录,下发t票
         */
        String ticket = loginTicketService.addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 用户登出:将该用户的t票信息的状态修改为1，即过期状态
     * @param ticket
     */
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket, 1);
    }

    public User getUser(int id){
        return userDao.selectById(id);
    }

    public User selectByName(String name){
        return userDao.selectByName(name);
    }


}
