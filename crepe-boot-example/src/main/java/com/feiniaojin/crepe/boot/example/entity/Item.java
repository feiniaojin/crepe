package com.feiniaojin.crepe.boot.example.entity;

import java.util.Date;

/**
 * item
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class Item {
    private Long id;
    private String itemName;
    private Integer itemCount;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemCount=" + itemCount +
                ", createTime=" + createTime +
                '}';
    }
}
