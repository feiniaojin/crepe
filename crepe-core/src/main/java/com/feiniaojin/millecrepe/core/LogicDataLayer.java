package com.feiniaojin.millecrepe.core;

import java.util.List;

/**
 * 逻辑数据层
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class LogicDataLayer {

    private List<LogicDataBase> logicDataBases;

    public LogicDataLayer() {
    }

    public LogicDataLayer(List<LogicDataBase> logicDataBases) {
        this.logicDataBases = logicDataBases;
    }

    public List<LogicDataBase> getLogicDataBases() {
        return logicDataBases;
    }

    public void setLogicDataBases(List<LogicDataBase> logicDataBases) {
        this.logicDataBases = logicDataBases;
    }

    public static final class Builder {
        private List<LogicDataBase> logicDataBases;

        private Builder() {
        }

        public static Builder aLogicDataLayer() {
            return new Builder();
        }

        public Builder withLogicDataBases(List<LogicDataBase> logicDataBases) {
            this.logicDataBases = logicDataBases;
            return this;
        }

        public LogicDataLayer build() {
            return new LogicDataLayer(logicDataBases);
        }
    }
}
