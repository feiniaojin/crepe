package com.feiniaojin.millecrepe.core;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象数据库
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class LogicDataBase {
    /**
     * 数据源
     */
    private DataSource dataSource;
    /**
     * table的索引
     */
    private List<String> tableIndexList;

    /**
     * 起始表索引
     */
    private String startIndex;

    /**
     * 结束表索引
     */
    private String endIndex;

    /**
     * 是否在左边填充0
     */
    private Boolean paddingZeroLeft;

    /**
     * 位数
     */
    private Integer tableIndexLength;

    public LogicDataBase(DataSource dataSource,
                         String startIndex,
                         String endIndex,
                         Integer indexLength,
                         Boolean paddingZeroLeft) {
        this.dataSource = dataSource;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tableIndexLength = tableIndexLength;
        this.paddingZeroLeft = paddingZeroLeft;
        initTableIndexList();
    }

    private void initTableIndexList() {
        int start = Integer.parseInt(startIndex);
        int end = Integer.parseInt(endIndex);
        tableIndexList = new ArrayList<>();
        int numLength;
        for (int i = start; i <= end; i++) {
            numLength = getNumLength(i);
            if (numLength < tableIndexLength && paddingZeroLeft) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < tableIndexLength - numLength; j++) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(i);
                tableIndexList.add(stringBuilder.toString());
            } else {
                tableIndexList.add(String.valueOf(i));
            }
        }
    }

    private static int getNumLength(long num) {
        num = num > 0 ? num : -num;
        if (num == 0) {
            return 1;
        }
        return (int) Math.log10(num) + 1;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getTableIndex() {
        return tableIndexList;
    }


    public static final class Builder {
        private DataSource dataSource;
        private List<String> tableIndexList;
        private String startIndex;
        private String endIndex;
        private Boolean paddingZeroLeft;
        private Integer tableIndexLength;

        private Builder() {
        }

        public static Builder aLogicDataBase() {
            return new Builder();
        }

        public Builder withDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder withTableIndexList(List<String> tableIndexList) {
            this.tableIndexList = tableIndexList;
            return this;
        }

        public Builder withStartIndex(String startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder withEndIndex(String endIndex) {
            this.endIndex = endIndex;
            return this;
        }

        public Builder withPaddingZeroLeft(Boolean paddingZeroLeft) {
            this.paddingZeroLeft = paddingZeroLeft;
            return this;
        }

        public Builder withTableIndexLength(Integer tableIndexLength) {
            this.tableIndexLength = tableIndexLength;
            return this;
        }

        public LogicDataBase build() {
            return new LogicDataBase(dataSource, startIndex, endIndex, tableIndexLength, paddingZeroLeft);
        }
    }
}
