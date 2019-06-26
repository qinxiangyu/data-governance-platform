package com.weiyi.hlj.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by qinxy on 2019/4/2.
 */
public class Commons {

    private static Logger logger = LoggerFactory.getLogger(Commons.class);

    public static String formatNow() {
        return DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
    }

    public static String md5(String source) {
        return DigestUtils.md5Hex(source);
    }

    public static long compareTime(String time){
        long now = System.currentTimeMillis();
        logger.info("compateTime time:{},now:{}",time,now);
        if(NumberUtils.isNumber(time)) {
            return now - Long.parseLong(time);
        }
        return 0;
    }

}
