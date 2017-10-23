package com.wanwan.nowcoder.interceptor;

import com.wanwan.nowcoder.dao.LoginTicketDao;
import com.wanwan.nowcoder.dao.UserDao;
import com.wanwan.nowcoder.model.HostHolder;
import com.wanwan.nowcoder.model.LoginTicket;
import com.wanwan.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: Wanwan Jiang
 * @Description: 拦截器一：根据t票判断该用户是谁，并保存到hostHolder中，供后面的服务使用
 * @Date: Created in 9:10 2017/10/12
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    /**
     * 判断用户是否已经登录（即判断是否带有t票，以及t票是否有效）
     * 1、如果已经登录，则将该t票对应的用户信息存放到hostHolder中，保证后面所有的服务都能通过hostHolder访问用户信息
     * 2、如果没有登录，则直接返回true
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;

        if (httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null){
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            // 用户未登录情况：
            // 1、t票不存在
            // 2、t票过期
            // 3、t票状态不为0，无效状态
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                return true;
            }

            //用户已登录
            User user = userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null){
            //前端可以通过${user}直接访问hostHolder中的user信息
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
