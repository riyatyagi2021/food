package com.mobcoder.kitchen.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class TTT {

    // 2021-07-20T21:00:00
    public static void main(String[] s) {

        //String created = "2021-07-20T16:30:00.659Z";
        String created = "2021-07-20T15:37:00";

       // String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

       // String UTC_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";

        DateFormat format = new SimpleDateFormat(UTC_FORMAT);

        DateFormat outputFormat = new SimpleDateFormat(UTC_FORMAT);
        outputFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        try {
            Date date = format.parse(created);
            String str = outputFormat.format(date);
            System.out.println(str);
        } catch (Exception e) {
        }
    }
}
