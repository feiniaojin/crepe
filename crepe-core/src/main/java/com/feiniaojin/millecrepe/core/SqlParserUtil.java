package com.feiniaojin.millecrepe.core;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL解析工具
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class SqlParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(SqlParserUtil.class);

    /**
     * 强制加上limit,最多取1000条
     *
     * @param sql
     * @param rowCount
     * @return
     */
    public static String forceAddLimit(String sql, Long rowCount) {
        try {
            Statement stmt = CCJSqlParserUtil.parse(sql);
            Select select = (Select) stmt;
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            Limit limit = selectBody.getLimit();
            if (limit == null) {
                Limit newLimit = new Limit();
                newLimit.setOffset(new LongValue(0L));
                newLimit.setRowCount(new LongValue(rowCount));
                selectBody.setLimit(newLimit);
                return select.toString();
            } else {
                //返回原SQL
                return sql;
            }
        } catch (JSQLParserException e) {
            logger.error("强制加上limit发生异常,sql=[{}],limit.rowCount=[{}]", sql, rowCount, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成下一个需要执行的SQL
     *
     * @param sql        待处理sql
     * @param tableIndex table的索引
     * @return 返回处理后的SQL
     */
    public static String nextSql(String sql, String tableIndex) {

        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            FromItem fromItem = selectBody.getFromItem();
            fromItem.accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(Table table) {
                    table.setName(table.getName() + tableIndex);
                }
            });
            return select.toString();
        } catch (JSQLParserException e) {
            logger.error("生成下个SQL失败,sql=[{}],tableIndex=[{}]",
                    sql,
                    tableIndex,
                    e);
            throw new RuntimeException(e);
        }
    }

    public static String addMilestoneInit(String sql, String milestoneName) {

        try {
            Statement stmt = CCJSqlParserUtil.parse(sql);
            Select select = (Select) stmt;
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            Expression where = selectBody.getWhere();
            if (where != null) {
                ExpressionDeParser expressionVisitor = new ExpressionDeParser();
                where.accept(expressionVisitor);
                //添加新的列
                StringBuilder buffer = expressionVisitor.getBuffer();
                buffer.append(" AND ").append(milestoneName).append(" > ").append("?");
            } else {
                selectBody.setWhere(CCJSqlParserUtil.parseCondExpression(milestoneName + " > " + "?"));
            }
            return select.toString();
        } catch (Exception e) {
            logger.error("加入初始化里程碑失败,sql=[{}],milestoneName=[{}]",
                    sql,
                    milestoneName,
                    e);
            throw new RuntimeException(e);
        }
    }
}
