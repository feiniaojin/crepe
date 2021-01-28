package com.feiniaojin.millecrepe.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TODO:Add the description of this class.
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class CrepeQuery<T> {

    private Logger logger = LoggerFactory.getLogger(CrepeQuery.class);

    /**
     * 当前数据源
     */
    private DataSource dataSource;

    /**
     * 参数
     */
    private LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();

    private ObjectMapper objectMapper;

    /**
     * 需要执行的SQL
     */
    private String sql = null;

    public T queryOne(String sql, Object currentMilestoneValue) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, currentMilestoneValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            T instance = null;
            if (resultSet.next()) {
                instance = (T) objectMapper.mapToObject(resultSet);
            }
            logger.debug("query的instance：" + instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> queryBatch(String sql, Object currentMilestoneValue) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, currentMilestoneValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add((T) objectMapper.mapToObject(resultSet));
            }
            logger.debug("query的list：" + list);
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LinkedHashMap<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(LinkedHashMap<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
