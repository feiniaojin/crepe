package com.feiniaojin.millecrepe.core.test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:Add the description of this class.
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class DruidTest {

    String sql = "select a,b,c,d from t_test where a=? and b=? and c=10 and id>?";

    @Test
    public void modifySQL() throws JSQLParserException {

        Statement stmt = CCJSqlParserUtil.parse(sql);
        Select select = (Select) stmt;
        SelectUtils.addExpression(select, new Column("f"));
        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        Expression where = selectBody.getWhere();
        ExpressionDeParser expressionVisitor = new ExpressionDeParser();
        where.accept(expressionVisitor);
        //添加新的列
        StringBuilder buffer = expressionVisitor.getBuffer();
        buffer.append(" AND c = ?");
        // 解析条件表达式
        where = CCJSqlParserUtil.parseCondExpression(buffer.toString());
        // 设置条件表达式
        selectBody.setWhere(where);

        //添加limit
        Limit limit = selectBody.getLimit();
        if (limit != null) {
            LongValue offset = (LongValue) limit.getOffset();
            offset.setValue(10L);
            LongValue rowCount = (LongValue) limit.getRowCount();
            // get获取, 修改为获取111条
            rowCount.setValue(11000);
            selectBody.setLimit(limit);
        } else {
            Limit newLimit = new Limit();
//            Expression expression = CCJSqlParserUtil.parseExpression("limit");
            LongValue offset = new LongValue("0");
            LongValue rowCount = new LongValue("1111");
            newLimit.setOffset(offset);
            newLimit.setRowCount(rowCount);
            selectBody.setLimit(newLimit);
        }


        //修改表名
        FromItem fromItem = selectBody.getFromItem();
        fromItem.accept(new FromItemVisitorAdapter() {
            @Override
            public void visit(Table table) {
                table.setName(table.getName() + "_001");
            }
        });

        System.out.println(fromItem.toString());
        System.out.println(select.toString());
        ReplaceColumnAndLongValues r = new ReplaceColumnAndLongValues();
        where.accept(r);
    }

    //获得where的列
    static class ReplaceColumnAndLongValues extends ExpressionDeParser {

        private List<String> whereConditions = new ArrayList<>();

        @Override
        public void visit(Column tableColumn) {
            System.out.println(tableColumn.getColumnName());
            whereConditions.add(tableColumn.getColumnName());
            super.visit(tableColumn);

        }
    }

    @Test
    public void testParseWhere() {
        try {
            String sql = "select * from t_a where x=? and  a=1  and b=?";
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            SelectBody selectBody = select.getSelectBody();
            PlainSelect plainSelect = (PlainSelect) selectBody;

            plainSelect.getWhere().accept(new ExpressionVisitorAdapter() {
               /* @Override
                public void visit(JdbcNamedParameter parameter) {
                    super.visit(parameter);
                    System.out.println(parameter);
                }*/

                /*@Override
                public void visit(JdbcParameter parameter) {
                    super.visit(parameter);

                    System.out.println(parameter.getIndex());
                    System.out.println(parameter);
                }*/

                /*@Override
                public void visit(SignedExpression expr) {
                    super.visit(expr);
                    System.out.println(expr);
                }*/

               /* @Override
                public void visit(NullValue value) {
                    super.visit(value);
                    System.out.println(value);
                }*/

                /*@Override
                public void visit(AndExpression expr) {
                    super.visit(expr);
                    System.out.println(expr);
                }*/

              /*  @Override
                public void visit(ExpressionList expressionList) {
                    super.visit(expressionList);
                    System.out.println(expressionList);
                }*/
/*
                @Override
                public void visit(NamedExpressionList namedExpressionList) {
                    super.visit(namedExpressionList);
                    System.out.println(namedExpressionList);
                }*/

               /* @Override
                public void visit(JdbcNamedParameter parameter) {
                    super.visit(parameter);
                    System.out.println(parameter);
                }*/

                @Override
                public void visit(EqualsTo expr) {
                    super.visit(expr);
                    System.out.println(expr.getLeftExpression());
                    System.out.println(expr.getRightExpression());
                    System.out.println(expr);
                }
            });
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }
}
