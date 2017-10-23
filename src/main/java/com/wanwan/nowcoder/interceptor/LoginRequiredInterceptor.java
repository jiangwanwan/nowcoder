package com.wanwan.nowcoder.interceptor;

import com.wanwan.nowcoder.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Wanwan Jiang
 * @Description: 拦截器二：当用户打开一个页面时，判断一下该用户是否具有访问该页面的权限。如果没有，直接跳转到登陆页面
 * @Date: Created in 16:49 2017/10/12
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 用户没有登陆,直接跳转到登陆页面
        if (hostHolder.getUser() == null){
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
