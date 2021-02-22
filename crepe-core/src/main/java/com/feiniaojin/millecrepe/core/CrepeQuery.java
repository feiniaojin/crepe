package com.feiniaojin.millecrepe.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    private ObjectMapper objectMapper;

    /**
     * 需要执行的SQL
     */
    private String sql = null;

    @SuppressWarnings("unchecked")
    public T queryOne(String sql, Object currentMilestoneValue) {
        logger.debug("queryBatch:dataSource=[{}],sql=[{}],currentMilestoneValue=[{}]", dataSource, sql, currentMilestoneValue);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, currentMilestoneValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            T instance = null;
            if (resultSet.next()) {
                instance = (T) objectMapper.mapToObject(resultSet);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> queryBatch(String sql, Object currentMilestoneValue) {
        logger.debug("queryBatch:dataSource=[{}],sql=[{}],currentMilestoneValue=[{}]", dataSource, sql, currentMilestoneValue);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, currentMilestoneValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add((T) objectMapper.mapToObject(resultSet));
            }
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

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
