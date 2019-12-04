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

    public static String getDateSimple() {
        DateTime dateTime = new DateTime(new Date());
        return dateTime.toString(DATE_FORMAT);
    }

    public static String timestampToDate(Long timestamp) {
        Date date = new Date(timestamp);
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DATE_FORMAT);
    }

}
