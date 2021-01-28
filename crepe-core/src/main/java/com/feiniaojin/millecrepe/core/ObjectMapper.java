package com.feiniaojin.millecrepe.core;

import java.sql.ResultSet;

/**
 * TODO:Add the description of this class.
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public interface ObjectMapper<T> {

    T mapToObject(ResultSet resultSet) throws Exception;

    Object getMilestoneValue(T t);
}
