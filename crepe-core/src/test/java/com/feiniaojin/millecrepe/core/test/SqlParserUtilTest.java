package com.feiniaojin.millecrepe.core.test;

import com.feiniaojin.millecrepe.core.SqlParserUtil;
import org.junit.Test;

/**
 * SqlParserUtil的测试类
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class SqlParserUtilTest {
    @Test
    public void testAddLimit() {
        String sql = "select * from t_a";
        System.out.println(SqlParserUtil.forceAddLimit(sql, 100L));
    }
}
