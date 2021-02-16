package com.feiniaojin.crepe.boot.example.controller;

import com.feiniaojin.crepe.boot.example.entity.Item;
import com.feiniaojin.millecrepe.core.Crepe;
import com.feiniaojin.millecrepe.core.CrepeBatchIterator;
import com.feiniaojin.millecrepe.core.LogicDataLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:Add the description of this class.
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource(name = "dataSourceDb0")
    private DataSource dataSourceDb0;

    @Resource(name = "dataSourceDb1")
    private DataSource dataSourceDb1;

    @Resource
    private Crepe<Item> crepe;

    @GetMapping("/test0")
    private Object get0() {
        try {
            PreparedStatement preparedStatement = dataSourceDb0.getConnection().prepareStatement("select * from t_item_000");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Object> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", resultSet.getObject("id"));
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test1")
    private Object get1() {
        try {
            PreparedStatement preparedStatement = dataSourceDb1.getConnection().prepareStatement("select * from t_item_000");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Object> list = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", resultSet.getObject("id"));
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test3")
    private Object get3() {
        List<Object> list = new ArrayList<>();
        for (Object object : crepe.open()) {
            logger.debug("返回的item=" + object);
            list.add(object);
        }
        return list;
    }

    @GetMapping("/test4")
    private Object get4() {
        List<Object> list = new ArrayList<>();
        CrepeBatchIterator<Item> batchIterator = crepe.openBatch();

        for (List<Item> tempList : crepe.openBatch()) {
            logger.debug("返回的tempList=" + tempList);
            list.add(tempList);
        }

        return list;
    }

}
