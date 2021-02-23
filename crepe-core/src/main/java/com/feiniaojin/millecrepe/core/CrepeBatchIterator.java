package com.feiniaojin.millecrepe.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * 批量迭代器，返回的是一个list
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class CrepeBatchIterator<T> implements Iterator<List<T>>, Iterable<List<T>> {

    private final Logger logger = LoggerFactory.getLogger(CrepeBatchIterator.class);

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

    private List<T> list = null;

    private ObjectMapper objectMapper;

    private CrepeBatchIterator(String originSql, LogicDataLayer logicDataLayer,
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

        list = null;
        crepeQuery.setDataSource(currentDataBase.getDataSource());
        //当前表的下一条SQL
        String nextSql = nextSql(this.originSql, currentTableIndex);
        //设置进去query对象
        crepeQuery.setSql(nextSql);

        while ((list = crepeQuery.queryBatch(nextSql, currentMilestoneValue)).size() == 0) {
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
                            currentMilestoneValue, nextSql);
                } else {
                    return false;
                }
            }
            nextSql = nextSql(this.originSql, currentTableIndex);
            crepeQuery.setSql(nextSql);
        }
        currentMilestoneValue = objectMapper.getMilestoneValue(list.get(list.size() - 1));
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
    public List<T> next() {
        return list;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return this;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Object getMilestoneInitValue() {
        return this.milestoneInitValue;
    }

    public void setMilestoneInitValue(Object milestoneInitValue) {
        this.milestoneInitValue = milestoneInitValue;
    }

    public Object getCurrentMilestoneValue() {
        return currentMilestoneValue;
    }

    public void setCurrentMilestoneValue(Object currentMilestoneValue) {
        this.currentMilestoneValue = currentMilestoneValue;
    }

    public static final class Builder<T> {
        LogicDataLayer logicDataLayer;
        private String milestoneName;
        private Object milestoneInitValue;
        private Object currentMilestoneValue;
        private String originSql;
        private LogicDataBase currentDataBase;
        private String currentTableIndex;
        private CrepeQuery<T> crepeQuery;
        private List<T> list = null;
        private ObjectMapper objectMapper;

        private Builder() {
        }

        public static <T> Builder<T> aCrepeBatchIterator() {
            return new Builder<T>();
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

        public Builder<T> withCurrentMilestoneValue(Object currentMilestoneValue) {
            this.currentMilestoneValue = currentMilestoneValue;
            return this;
        }

        public Builder<T> withOriginSql(String originSql) {
            this.originSql = originSql;
            return this;
        }

        public Builder<T> withCurrentDataBase(LogicDataBase currentDataBase) {
            this.currentDataBase = currentDataBase;
            return this;
        }

        public Builder<T> withCurrentTableIndex(String currentTableIndex) {
            this.currentTableIndex = currentTableIndex;
            return this;
        }

        public Builder<T> withCrepeQuery(CrepeQuery<T> crepeQuery) {
            this.crepeQuery = crepeQuery;
            return this;
        }

        public Builder<T> withList(List<T> list) {
            this.list = list;
            return this;
        }

        public Builder<T> withObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public CrepeBatchIterator<T> build() {
            return new CrepeBatchIterator<T>(originSql, logicDataLayer, objectMapper, milestoneName, milestoneInitValue);
        }
    }
}
