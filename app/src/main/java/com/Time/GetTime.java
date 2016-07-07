package com.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016/5/16.
 */
public class GetTime {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getTime(Date date){

       return simpleDateFormat.format(date);
    }
}
