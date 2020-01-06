package cn.edu.jxust.arrangeproduce.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author ZSS
 * @date 2020/1/6 19:35
 * @description 数值位数调整
 */
@Component
public class DigitUtil {

    /**
     * 数据位数转化,正数不变,负数去除负号
     *
     * @param source 源数据
     * @param digit  保存位数
     * @return String
     */
    public static String formatDigit(String source, Integer digit) {
        BigDecimal decimal = new BigDecimal(source).setScale(digit, BigDecimal.ROUND_UP);
        BigDecimal zeroDecimal = new BigDecimal(0);
        if (decimal.compareTo(zeroDecimal) >= 0) {
            return decimal.toString();
        } else {
            return decimal.toString().replace("-", "");
        }
    }

}
