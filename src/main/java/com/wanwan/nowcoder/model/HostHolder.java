package com.wanwan.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * @Author: Wanwan Jiang
 * @Description: 本地线程：每个线程拥有自己的用户信息
 * @Date: Created in 10:45 2017/10/12
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */

@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
