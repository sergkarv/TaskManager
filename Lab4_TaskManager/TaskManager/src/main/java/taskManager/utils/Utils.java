package taskManager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    //template date in xml file's
    //1980-03-23T10:20:15
    //because xs:dateTime
    public static Calendar strToCalendar(String s, boolean flagAndSecond){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTF+4"));
        StringBuffer template = new StringBuffer("yyyy'-'MM'-'dd'T'HH:mm");
        if(flagAndSecond){
            template.append(":ss");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(template.toString());
        try {
            java.util.Date date = dateFormat.parse(s);
            calendar.setTime(date);
            calendar.setTimeZone(TimeZone.getTimeZone("Etc/GMT+3"));
            int hour = date.getHours();
            int minute = date.getMinutes();
            int seconds = date.getSeconds();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            if(flagAndSecond){
                calendar.set(Calendar.SECOND, seconds);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String calendarToStr(Calendar calendar, boolean flagAndSecond){
        StringBuffer template = new StringBuffer("yyyy'-'MM'-'dd'T'HH:mm");
        if(flagAndSecond){
            template.append(":ss");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(template.toString());
        return dateFormat.format(calendar.getTime());
    }
}
