# Crepe

# 1. 简介

Crepe是一个分库分表场景下直连数据库进行遍历全量库表的组件。用户通过简单的配置之后，只需要通过一条SQL语句，通过一个for循环，即可遍历所有数据，极大地简化了分库分表下的离线数据处理任务

crepe提供两种操作场景：

第一种是单条处理的场景。每次查出一条，逐条处理

```java
for (Object object : crepe.open()) {
    //TODO 针对查询出来的object进行相应的处理
    System.out.println(object);
}
```

第二种是批量处理的场景。每次查出一批数据，逐批处理，处理完之后再返回下一批数据

```java
for (List<Item> items : crepe.openBatch()) {
    //TODO 针对查询出来的列表进行相应的处理，可以并发进行处理，提高处理效率
    System.out.println(items);
}
```

# 2. 整体架构



[![yLp4YD.png](https://s3.ax1x.com/2021/02/23/yLp4YD.png)](https://imgchr.com/i/yLp4YD)

# 3. quick start

* 引入maven依赖

```xml
<dependency>
    <groupId>com.feiniaojin</groupId>
    <artifactId>crepe-core</artifactId>
    <version>1.0.9-SNAPSHOT</version>
</dependency>
```

* 配置crepe

一个实际的数据库实例配置一个LogicDataBase，同时要配置表信息

> 以Spring Boot工程为例

```java
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
                .withName("db0")
                .build();
    }
    @Bean(name = "logicDataBaseDb1")
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

    @Bean(name = "logicDataLayer")
    public LogicDataLayer logicDataLayer(LogicDataBase logicDataBaseDb0,
                                         LogicDataBase logicDataBaseDb1
                                       	) {
        return LogicDataLayer.Builder.aLogicDataLayer()
                .withLogicDataBases(Arrays.asList(logicDataBaseDb0,
                        logicDataBaseDb1))
                .build();
    }
}
```

* 配置crepe实例

crepe是非常轻量的对象，建议每个业务单独配一个crepe

```java
 Crepe<Object> crepeMethod = Crepe.Builder.aCrepe()
                .withLogicDataLayer(logicDataLayer)
                .withMilestoneName("id")
                .withMilestoneInitValue("0")
                .withOriginSql("select * from t_item_  limit 100")
                .withObjectMapper(new ItemObjectMapper())
                .build();
```

* for循环遍历

crepe.open()：开启单个对象的迭代器

 crepe.openBatch()：开启批量的迭代器

```java
for (Object object : crepe.open()) {
    System.out.println(object);
}

for (List<Item> items : crepe.openBatch()) {
    System.out.println(items);
}
```

* 遍历过程设置

所有的场景都会从头开始遍历数据，有时候需要设置起始的数据库、数据表以及单表里程碑

> 术语：我们称当前遍历的库、表、里程碑为一组坐标

具体操作：

先开启迭代器，然后设置当前的库索引、表索引、以及单表的里程碑

**重要**：一定要执行iterator.hasNext()判断是否有数据，否则直接iterator.next()不会有数据返回

案例代码：

```java
//req为数据加载请求，里面包含了需要设置的坐标信息

CrepeBatchIterator<T> iterator = crepe.openBatch();

LogicDataBase logicDataBase =logicDataLayer.getLogicDataBases().get(req.getCurrentDbIndex());

iterator.setCurrentDataBase(logicDataBase);
iterator.setCurrentTableIndex(logicDataBase.getTableIndex().get(req.getCurrentTableIndex()));
iterator.setCurrentMilestoneValue(req.getCurrentMilestoneValue());

//重要：一定要执行iterator.hasNext()判断是否有数据，否则直接iterator.next()不会有数据返回
if (iterator.hasNext()) {
    List<CampusUserInfo> campusUserInfos = iterator.next();

    //当前库索引
    int indexOfDb = crepe.getLogicDataLayer().getLogicDataBases().indexOf(iterator.getCurrentDataBase());
    //当前表索引
    int indexOfTable = iterator.getCurrentDataBase().getTableIndex().indexOf(iterator.getCurrentTableIndex());
    //当前里程碑
    String currentMilestone = String.valueOf(iterator.getCurrentMilestoneValue());
} else {
    logger.info("加载批处理任务数据:无数据")
}
```

