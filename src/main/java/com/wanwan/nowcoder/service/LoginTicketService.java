package com.wanwan.nowcoder.service;

import com.wanwan.nowcoder.dao.LoginTicketDao;
import com.wanwan.nowcoder.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 8:05 2017/10/12
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Service
public class LoginTicketService {
    @Autowired
    LoginTicketDao loginTicketDao;

    /**
     * 当用户注册成功或登陆时，给userId对应的用户下发t票
     * @param userId
     * @return 下发的t票
     */
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        Date now = new Date();
        now.setTime(now.getTime() + 3600*24*100);       //有效期为100天
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }


}
