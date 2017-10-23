package com.wanwan.nowcoder.controller;

import com.wanwan.nowcoder.service.LoginTicketService;
import com.wanwan.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wanwan Jiang
 * @Description: 登陆注册控制器
 * @Date: Created in 19:45 2017/10/10
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Controller
public class LoginAndRegisterController {

    private static final Logger logger = LoggerFactory.getLogger(LoginAndRegisterController.class);

    @Autowired
    UserService userService;

    /**
     * 进入“登入注册”界面
     * @return
     */
    @RequestMapping(value = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model,
                           @RequestParam(value = "next", required = false) String next){
        model.addAttribute("next", next);
        return "reglogin";
    }

    /**
     * 注册
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = {"/reg/"}, method = {RequestMethod.POST})
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "next", required = false) String next,
                           @RequestParam(value = "remember", defaultValue = "false") boolean remember,
                           HttpServletResponse response){

        try {
            Map<String,String> map = userService.register(username, password);
            if (!map.containsKey("ticket")){    //注册失败
                model.addAttribute("msg", map.get("msg"));
                return "reglogin";
            }else { //注册成功，将t票下发到客户端
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (remember){
                    cookie.setMaxAge(3600*24*5); //cookie信息保存5天
                }
                response.addCookie(cookie);

                if (StringUtils.isEmpty(next)){
                    return "redirect:/";
                }else {
                    return "redirect:" + next;
                }
            }
        }catch (Exception e){
            logger.error("注册异常:" + e.getMessage());
            return "reglogin";
        }
    }

    /**
     * 登陆
     * @param model
     * @param username
     * @param password
     * @param remember
     * @return
     */
    @RequestMapping(value = {"/login/"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", defaultValue = "false") boolean remember,
                        @RequestParam(value = "next", required = false) String next,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(username,password);
            if (!map.containsKey("ticket")){    //登陆失败
                model.addAttribute("msg",map.get("msg"));
                return "reglogin";
            }else {     //登陆成功，将t票下发到客户端
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                if (remember){
                    cookie.setMaxAge(3600*24*5); //cookie信息保存5天
                }
                cookie.setPath("/");
                response.addCookie(cookie);

                if (StringUtils.isEmpty(next)){
                    return "redirect:/";
                }else {
                    return "redirect:" + next;
                }

            }


        }catch (Exception e){
            logger.error("登陆异常：" + e.getMessage());
            return "reglogin";
        }
    }

    /**
     * 登出
     * @param ticket
     * @return
     */
    @RequestMapping(value = {"/logout/"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


}
