package com.wanwan.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wanwan Jiang
 * @Description: 统一数据实体
 * @Date: Created in 21:46 2017/10/9
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
