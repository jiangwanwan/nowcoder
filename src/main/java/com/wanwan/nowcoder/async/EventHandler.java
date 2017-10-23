package com.wanwan.nowcoder.async;

import java.util.List;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 13:46 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
public interface EventHandler {
    void doHandle(EventModel model);        //处理操作
    List<EventType> getSupportEventTypes();     //注册所关注的事件
}
