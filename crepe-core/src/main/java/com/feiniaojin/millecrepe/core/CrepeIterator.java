package com.feiniaojin.millecrepe.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * 迭代器
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class CrepeIterator<T> implements Iterator<T>, Iterable<T> {

    private final Logger logger = LoggerFactory.getLogger(CrepeIterator.class);

    LogicDataLayer logicDataLayer;

    /**
     * 里程碑名称
     */
    private String milestoneName;

    /**
     * 里程碑初始值
     */
    private Object milestoneInitValue;

    /**
     * 里程碑初始值
     */
    private Object currentMilestoneValue;

    /**
     * 原始sql
     */
    private String originSql;

    /**
     * 当前数据源
     */
    private LogicDataBase currentDataBase;

    /**
     * 当前表索引
     */
    private String currentTableIndex;

    /**
     * CrepeQuery 通过query得到
     */
    private CrepeQuery<T> crepeQuery;

    private T t = null;

    private ObjectMapper objectMapper;

    private CrepeIterator(String originSql, LogicDataLayer logicDataLayer,
                          ObjectMapper objectMapper,
                          String milestoneName,
                          Object milestoneInitValue) {
        this.logicDataLayer = logicDataLayer;
        this.objectMapper = objectMapper;
        this.milestoneName = milestoneName;
        this.originSql = originSql;
        this.milestoneInitValue = milestoneInitValue;
        this.currentMilestoneValue = milestoneInitValue;
        init(originSql, logicDataLayer, objectMapper, milestoneName, milestoneInitValue);

    }

    private void init(String originSql,
                      LogicDataLayer logicDataLayer,
                      ObjectMapper objectMapper,
                      String milestoneName,
                      Object milestoneInitValue) {
        currentDataBase = logicDataLayer.getLogicDataBases().get(0);
        currentTableIndex = currentDataBase.getTableIndex().get(0);
        this.originSql = addMilestoneInit(originSql, milestoneName);
        crepeQuery = new CrepeQuery<>();
        crepeQuery.setDataSource(currentDataBase.getDataSource());
        crepeQuery.setObjectMapper(objectMapper);
        checkSelect(originSql);
    }

    private void checkSelect(String originSql) {
        if (!SqlParserUtil.isSelect(originSql)) {
            throw new RuntimeException("Not a select SQL");
        }
    }

    /**
     * 加入设置里程碑
     *
     * @param sql           查询sql
     * @param milestoneName 里程碑的列名
     * @return sql
     */
    private String addMilestoneInit(String sql, String milestoneName) {
        return SqlParserUtil.addMilestoneInit(sql, milestoneName);
    }

    @Override
    public boolean hasNext() {

        t = null;
        //当前表的下一条SQL
        String nextSql = nextSql(this.originSql, currentTableIndex);
        //设置进去query对象
        crepeQuery.setSql(nextSql);

        while ((t = crepeQuery.queryOne(nextSql, currentMilestoneValue)) == null) {
            //切换表
            List<String> tableIndexes = currentDataBase.getTableIndex();
            int tableIndexOf = tableIndexes.indexOf(currentTableIndex);
            if (tableIndexOf != -1 && tableIndexOf < tableIndexes.size() - 1) {
                currentTableIndex = tableIndexes.get(tableIndexOf + 1);
                currentMilestoneValue = milestoneInitValue;
                logger.debug("切换表并重置里程碑,currentDb=[{}],currentTableIndex=[{}],currentMilestoneValue=[{}],sql=[{}]",
                        currentDataBase.getName(),
                        currentTableIndex,
                        currentMilestoneValue,
                        nextSql);
            } else {
                //切换库，并且重置tableIndex
                List<LogicDataBase> logicDataBases = logicDataLayer.getLogicDataBases();
                int indexOf = logicDataBases.indexOf(currentDataBase);
                if (indexOf < logicDataBases.size() - 1) {
                    currentDataBase = logicDataBases.get(indexOf + 1);
                    crepeQuery.setDataSource(currentDataBase.getDataSource());
                    //新的表index从0开始
                    currentTableIndex = currentDataBase.getTableIndex().get(0);
                    currentMilestoneValue = milestoneInitValue;
                    logger.debug("切换库并切换表并重置里程碑,currentDb=[{}],currentTableIndex=[{}],currentMilestoneValue=[{}],sql=[{}]",
                            currentDataBase.getName(),
                            currentTableIndex,
                            currentMilestoneValue,
                            nextSql);
                } else {
                    t = null;
                    return false;
                }
            }
            nextSql = nextSql(this.originSql, currentTableIndex);
            crepeQuery.setSql(nextSql);
        }
        currentMilestoneValue = objectMapper.getMilestoneValue(t);
        //取出里程碑的值
        return true;
    }

    /**
     * 生成下一个需要执行的SQL
     *
     * @param sql
     * @param tableIndex
     * @return
     */
    private String nextSql(String sql, String tableIndex) {
        return SqlParserUtil.nextSql(sql, tableIndex);
    }

    @Override
    public T next() {
        return t;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public void forEach(Consumer<? super T> action) {

    }

    public LogicDataLayer getLogicDataLayer() {
        return logicDataLayer;
    }

    public void setLogicDataLayer(LogicDataLayer logicDataLayer) {
        this.logicDataLayer = logicDataLayer;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getOriginSql() {
        return originSql;
    }

    public void setOriginSql(String originSql) {
        this.originSql = originSql;
    }

    public LogicDataBase getCurrentDataBase() {
        return currentDataBase;
    }

    public void setCurrentDataBase(LogicDataBase currentDataBase) {
        this.currentDataBase = currentDataBase;
    }

    public String getCurrentTableIndex() {
        return currentTableIndex;
    }

    public void setCurrentTableIndex(String currentTableIndex) {
        this.currentTableIndex = currentTableIndex;
    }

    public CrepeQuery<T> getCrepeQuery() {
        return crepeQuery;
    }

    public void setCrepeQuery(CrepeQuery<T> crepeQuery) {
        this.crepeQuery = crepeQuery;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public static final class Builder<T> {
        LogicDataLayer logicDataLayer = new LogicDataLayer();
        CrepeQuery<T> crepeQuery;
        T t = null;
        private String milestoneName;
        private Object milestoneInitValue;
        private String sql;
        private ObjectMapper objectMapper;

        private Builder() {
        }

        public static <T> Builder<T> aCrepeIterator() {
            return new Builder<>();
        }

        public Builder<T> withLogicDataLayer(LogicDataLayer logicDataLayer) {
            this.logicDataLayer = logicDataLayer;
            return this;
        }

        public Builder<T> withMilestoneName(String milestoneName) {
            this.milestoneName = milestoneName;
            return this;
        }

        public Builder<T> withMilestoneInitValue(Object milestoneInitValue) {
            this.milestoneInitValue = milestoneInitValue;
            return this;
        }

        public Builder<T> withOriginSql(String sql) {
            this.sql = sql;
            return this;
        }

        public Builder<T> withCrepeQuery(CrepeQuery<T> crepeQuery) {
            this.crepeQuery = crepeQuery;
            return this;
        }

        public Builder<T> withObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public CrepeIterator<T> build() {
            return new CrepeIterator<T>(sql, logicDataLayer, objectMapper, milestoneName, milestoneInitValue);
        }
    }
}
