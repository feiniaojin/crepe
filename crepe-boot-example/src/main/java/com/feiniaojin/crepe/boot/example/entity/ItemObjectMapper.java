package com.feiniaojin.crepe.boot.example.entity;

import com.feiniaojin.millecrepe.core.ObjectMapper;

import java.sql.ResultSet;

/**
 * Item类的封装
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class ItemObjectMapper implements ObjectMapper<Item> {

    @Override
    public Item mapToObject(ResultSet resultSet) throws Exception {
        Item item = new Item();
        item.setId((Long) resultSet.getObject("id"));
        item.setItemName((String) resultSet.getObject("item_name"));
        item.setItemCount((Integer) resultSet.getObject("item_count"));
        item.setCreateTime(resultSet.getDate("create_time"));
        return item;
    }

    @Override
    public Object getMilestoneValue(Item item) {
        return item.getId();
    }
}
