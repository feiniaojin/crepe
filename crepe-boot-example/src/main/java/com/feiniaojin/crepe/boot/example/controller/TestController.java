package com.feiniaojin.crepe.boot.example.controller;

import com.feiniaojin.crepe.boot.example.entity.Item;
import com.feiniaojin.crepe.boot.example.entity.ItemObjectMapper;
import com.feiniaojin.millecrepe.core.Crepe;
import com.feiniaojin.millecrepe.core.CrepeBatchIterator;
import com.feiniaojin.millecrepe.core.LogicDataBase;
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

    @Resource
    private LogicDataLayer logicDataLayer;

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
    private Object test3() {
        List<Object> list = new ArrayList<>();


        for (Object object : crepe.open()) {
            System.out.println(object);
        }

        for (List<Item> items : crepe.openBatch()) {
            System.out.println(items);
        }

//        list.add(object);
        return list;
    }

    @GetMapping("/test5")
    private Object test5() {
        List<Object> list = new ArrayList<>();


        for (List<Item> items : crepe.openBatch()) {
            System.out.println(items);
        }




        return list;
    }

    @GetMapping("/test4")
    private Object test4(String dbIndex, String tableIndex, String milestone) {

        Crepe<Object> crepeMethod = Crepe.Builder.aCrepe()
                .withLogicDataLayer(logicDataLayer)
                .withMilestoneName("id")
                .withMilestoneInitValue("0")
                .withOriginSql("select * from t_item_  limit 100")
                .withObjectMapper(new ItemObjectMapper())
                .build();

        CrepeBatchIterator<Object> iterator = crepeMethod.openBatch();

        LogicDataBase logicDataBase = logicDataLayer.getLogicDataBases().get(Integer.valueOf(dbIndex));
        iterator.setCurrentDataBase(logicDataBase);

        String tableIndexSeq = logicDataBase.getTableIndex().get(Integer.valueOf(tableIndex));
        iterator.setCurrentTableIndex(tableIndexSeq);

        iterator.setCurrentMilestoneValue(milestone);

        List<Object> next = null;
        if (iterator.hasNext()) {
            next = iterator.next();
        } else {
            next = new ArrayList<>();
        }
        logger.info("当前数据库为：" + logicDataLayer.getLogicDataBases().indexOf(iterator.getCurrentDataBase()));
        logger.info("当前数据表为：" + iterator.getCurrentDataBase().getTableIndex().indexOf(iterator.getCurrentTableIndex()));
        logger.info("当前里程碑为：" + iterator.getCurrentMilestoneValue());
//        logger.debug("返回的tempList=" + next);
        List<Object> list = new ArrayList<>(next);

        return list;
    }

}
