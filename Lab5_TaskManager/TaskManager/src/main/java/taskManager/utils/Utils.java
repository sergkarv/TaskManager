package taskManager.utils;

import taskManager.dao.GenericDao;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.Taskweb;
import taskManager.domain.User;
import taskManager.domain.Userweb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    public static User userConvert(Userweb userweb){
        User user = new User();
        if(userweb.getId() != null){
            user.setId( new Integer(userweb.getId()) );
        }
        user.setName(userweb.getName());
        user.setPassword(userweb.getPassword());
        return user;
    }

    public static Userweb userConvert(User user){
        Userweb userweb = new Userweb();
        if(user.getId() != null){
            userweb.setId( new Integer(user.getId()) );
        }
        userweb.setName(user.getName());
        userweb.setPassword(user.getPassword());
        return userweb;
    }

    public static Task taskConvert(Taskweb taskweb, GenericDao<Task, Integer> taskDao, GenericDao<User, Integer> userDao){
        Task task = new Task();
        if(taskweb.getId() != null){
            task.setId(new Integer(taskweb.getId()));
        }
        task.setName(taskweb.getName());
        task.setContacts(taskweb.getContacts());
        task.setDescription(taskweb.getDescription());
        if(taskweb.getDate() != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskweb.getDate());
            task.setDate(calendar.getTime() );
        }
        task.setHighPriority(taskweb.isHighPriority());
        try {
            task.setParent((taskweb.getParentId()!=null) ? taskDao.getByPK(taskweb.getParentId()):null);
        } catch (PersistException e) {
            e.printStackTrace();
        }
        try {
            task.setUser((taskweb.getUserId()!=null) ? userDao.getByPK(taskweb.getUserId()):null);
        } catch (PersistException e) {
            e.printStackTrace();
        }
        return task;
    }

    public static Taskweb taskConvert(Task task){
        Taskweb taskweb = new Taskweb();
        if(task.getId() != null){
            taskweb.setId(new Integer(task.getId()));
        }
        taskweb.setName(task.getName());
        taskweb.setContacts(task.getContacts());
        taskweb.setDescription(task.getDescription());
        Calendar calendar = Calendar.getInstance();
        if(task.getDate() != null){
            calendar.setTime(task.getDate());
            taskweb.setDate(calendar.getTime());
        }
        taskweb.setHighPriority(task.isHighPriority());
        if(task.getParent() != null){
            taskweb.setParentId(new Integer( task.getParent().getId() ) );
        }
        if(task.getUser() != null){
            taskweb.setUserId(new Integer( task.getUser().getId() ) );
        }
        return taskweb;
    }
}
