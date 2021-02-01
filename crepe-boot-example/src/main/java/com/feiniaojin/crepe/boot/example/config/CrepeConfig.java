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

    @Bean
    public LogicDataBase logicDataBaseDb0() {
        return LogicDataBase.Builder.aLogicDataBase()
                .withDataSource(dataSourceDb0)
                .withStartIndex("0")
                .withEndIndex("3")
                .withPaddingZeroLeft(Boolean.TRUE)
                .withTableIndexLength(3)
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
                .build();
    }

    @Bean
    public LogicDataLayer logicDataLayer() {
        return LogicDataLayer.Builder.aLogicDataLayer()
                .withLogicDataBases(Arrays.asList(logicDataBaseDb0(), logicDataBaseDb1()))
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
