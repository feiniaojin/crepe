package com.feiniaojin.crepe.boot.example.config;

import com.feiniaojin.crepe.boot.example.entity.Item;
import com.feiniaojin.crepe.boot.example.entity.ItemObjectMapper;
import com.feiniaojin.millecrepe.core.Crepe;
import com.feiniaojin.millecrepe.core.LogicDataBase;
import com.feiniaojin.millecrepe.core.LogicDataLayer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Crepe的配置
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
@Configuration
public class CrepeConfig {

    @Resource(name = "dataSourceDb0")
    private DataSource dataSourceDb0;
    @Resource(name = "dataSourceDb1")
    private DataSource dataSourceDb1;
    @Resource(name = "dataSourceDb2")
    private DataSource dataSourceDb2;
    @Resource(name = "dataSourceDb3")
    private DataSource dataSourceDb3;
    @Resource(name = "dataSourceDb4")
    private DataSource dataSourceDb4;
    @Resource(name = "dataSourceDb5")
    private DataSource dataSourceDb5;

    @Bean
    public LogicDataBase logicDataBaseDb0() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb0)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
                .withName("db0")
                .build();
    }
    @Bean
    public LogicDataBase logicDataBaseDb1() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb1)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
                .withName("db1")
                .build();
    }
    @Bean
    public LogicDataBase logicDataBaseDb2() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb2)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
                .withName("db2")
                .build();
    }
    @Bean
    public LogicDataBase logicDataBaseDb3() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb3)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
                .withName("db3")
                .build();
    }
    @Bean
    public LogicDataBase logicDataBaseDb4() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb4)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
                .withName("db4")
                .build();
    }
    @Bean
    public LogicDataBase logicDataBaseDb5() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb5)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
                .withName("db5")
                .build();
    }

    @Bean
    public LogicDataLayer logicDataLayer() {
        return LogicDataLayer.Builder.aLogicDataLayer()
                .withLogicDataBases(Arrays.asList(logicDataBaseDb0(),
                        logicDataBaseDb1(),
                        logicDataBaseDb2(),
                        logicDataBaseDb3(),
                        logicDataBaseDb4(),
                        logicDataBaseDb5()))
                .build();
    }

    @Bean
    public Crepe<Item> crepe() {
        return Crepe.Builder.aCrepe().withLogicDataLayer(logicDataLayer())
                .withMilestoneName("id")
                .withMilestoneInitValue("0")
                .withOriginSql("select * from t_item_ limit 1000")
                .withObjectMapper(new ItemObjectMapper())
                .build();
    }
}
