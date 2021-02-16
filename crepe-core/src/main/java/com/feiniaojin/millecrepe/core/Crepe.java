package com.feiniaojin.millecrepe.core;

/**
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class Crepe<T> {

    LogicDataLayer logicDataLayer;

    private String originSql;

    /**
     * 里程碑名称
     */
    private String milestoneName;

    /**
     * 里程碑初始值
     */
    private Object milestoneInitValue;

    /**
     * 结果对象封装
     */
    private ObjectMapper objectMapper;

    public CrepeIterator<T> open() {
        return (CrepeIterator<T>) CrepeIterator.Builder.aCrepeIterator()
                .withLogicDataLayer(logicDataLayer)
                .withOriginSql(originSql)
                .withMilestoneName(milestoneName)
                .withMilestoneInitValue(milestoneInitValue)
                .withObjectMapper(objectMapper)
                .build();
    }

    public CrepeBatchIterator<T> openBatch() {
        return (CrepeBatchIterator<T>) CrepeBatchIterator.Builder.aCrepeBatchIterator()
                .withLogicDataLayer(logicDataLayer)
                .withOriginSql(originSql)
                .withMilestoneName(milestoneName)
                .withMilestoneInitValue(milestoneInitValue)
                .withObjectMapper(objectMapper)
                .build();
    }

    public LogicDataLayer getLogicDataLayer() {
        return logicDataLayer;
    }

    public void setLogicDataLayer(LogicDataLayer logicDataLayer) {
        this.logicDataLayer = logicDataLayer;
    }

    public static final class Builder {
        LogicDataLayer logicDataLayer = new LogicDataLayer();
        private String originSql;
        private String milestoneName;
        private Object milestoneInitValue;
        private ObjectMapper objectMapper;

        private Builder() {
        }

        public static Builder aCrepe() {
            return new Builder();
        }

        public Builder withLogicDataLayer(LogicDataLayer logicDataLayer) {
            this.logicDataLayer = logicDataLayer;
            return this;
        }

        public Builder withOriginSql(String originSql) {
            this.originSql = originSql;
            return this;
        }

        public Builder withMilestoneName(String milestoneName) {
            this.milestoneName = milestoneName;
            return this;
        }

        public Builder withMilestoneInitValue(Object milestoneInitValue) {
            this.milestoneInitValue = milestoneInitValue;
            return this;
        }

        public Builder withObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public <T> Crepe<T> build() {
            Crepe<T> crepe = new Crepe<>();
            crepe.milestoneName = this.milestoneName;
            crepe.milestoneInitValue = this.milestoneInitValue;
            crepe.objectMapper = this.objectMapper;
            crepe.originSql = this.originSql;
            crepe.logicDataLayer = this.logicDataLayer;
            return crepe;
        }
    }
}
