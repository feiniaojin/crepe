package com.feiniaojin.millecrepe.core;

/**
 * 简单版的StringUtils，避免引入外部jar包
 *
 * @author: <a href=mailto:943868899@qq.com>Yujie</a>
 */
public class SimpleStringUtils {

    /**
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
}
