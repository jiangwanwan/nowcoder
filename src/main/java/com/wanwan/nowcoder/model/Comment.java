package com.wanwan.nowcoder.model;

import java.util.Date;

/**
 * @Author: Wanwan Jiang
 * @Description: 评论实体
 * @Date: Created in 14:51 2017/10/14
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
public class Comment {
    private int id;
    private String content;
    private int userId;
    private int entityId;
    private int entityType;
    private Date createdDate;
    private int status;

    public Comment() {
    }

    public Comment(int id, String content, int userId, int entityId, int entityType, Date createdDate, int status) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
        this.createdDate = createdDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", entityId=" + entityId +
                ", entityType=" + entityType +
                ", createdDate=" + createdDate +
                ", status=" + status +
                '}';
    }
}
