package cn.edu.jxust.arrangeproduce.util;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ZSS
 * @date 2019/12/4 10:35
 * @description 时间工具
 */
@Component
public class DateUtil {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String DATE_FORMAT_COMPLETE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间(简单格式)
     *
     * @return String
     */
    public static String getDateSimple() {
        DateTime dateTime = new DateTime(new Date());
        return dateTime.toString(DATE_FORMAT);
    }

    /**
     * 时间戳转String
     *
     * @param timestamp 时间戳
     * @return String
     */
    public static String timestampToDate(Long timestamp) {
        Date date = new Date(timestamp);
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DATE_FORMAT);
    }

    /**
     * 获取当前时间(完整模式)
     *
     * @return String
     */
    public static String getDateComplete() {
        DateTime dateTime = new DateTime((new Date()));
        return dateTime.toString(DATE_FORMAT_COMPLETE);
    }

}
