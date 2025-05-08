package com.minecraft.minepay.util.time;

import com.minecraft.minepay.Core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    public static String format(Object object) {
        try {
            SimpleDateFormat dateFormat = Core.DATE_FORMAT;
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return dateFormat.format(object);
        } catch (Exception e) {
            return Core.DATE_FORMAT.format(object);
        }
    }

    public static String format(String pattern, Object object) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return dateFormat.format(object);
        } catch (Exception e) {
            return Core.DATE_FORMAT.format(object);
        }
    }

    public static String formatAPI(String source) {
        try {
            String pattern = "yyyy-MM-dd'T'hh:mm:ss";
            SimpleDateFormat sdfSource = new SimpleDateFormat(pattern);

            Date date = sdfSource.parse(source);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return source;
        }
    }

}
