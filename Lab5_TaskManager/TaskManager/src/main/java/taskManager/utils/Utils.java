package taskManager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Сергей on 14.05.16.
 */
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
            calendar.setTime(dateFormat.parse(s));
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
