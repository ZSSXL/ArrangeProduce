package cn.edu.jxust.arrangeproduce.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ZSS
 * @date 2020/1/13 15:38
 * @description 参数工具
 */
public class ParameterUtil {

    /**
     * default
     *
     * @param source       源数据
     * @param defaultValue 默认
     * @return String
     */
    public static String isEmpty(String source, String defaultValue) {
        return StringUtils.isEmpty(source) ? defaultValue : source;
    }

    /**
     * 默认null
     *
     * @param source 源数据
     * @return String
     */
    public static String isEmpty(String source) {
        return StringUtils.isEmpty(source) ? null : source;
    }
}
