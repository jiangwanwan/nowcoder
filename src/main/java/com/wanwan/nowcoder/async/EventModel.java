package com.wanwan.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Wanwan Jiang
 * @Description: 事件发生现场
 * @Date: Created in 13:18 2017/10/16
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
public class EventModel {
    private EventType type;     //事件
    private int actorId;        //触发者id（相当于fromId）
    private int entityId;       //实体id
    private int entityType;     //实体type
    private int entityOwnerId;  //实体拥有者（相当于toId）

    private Map<String, String> exts = new HashMap<>(); //扩展字段，保存其他字段信息

    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }


    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;
    }
}
