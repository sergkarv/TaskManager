package taskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import taskManager.dao.DaoFactory;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.*;
import taskManager.utils.Utils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Сергей on 18.04.16.
 */
@Controller
public class TaskController {
    @Autowired
    private DaoFactory<Connection> daoFactory;

    private Connection connection;

    @PostConstruct
     public void postTaskController() {
        try {
            connection = daoFactory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/taskslist", method = RequestMethod.GET)
    public ModelAndView tasksList() {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("taskslist");
        List<User> listUser = null;
        List<Task> listTask = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
            listUser = userDao.getAll();
            listTask = taskDao.getAll();
            modelAndView.addObject("userListJSP", listUser);
            modelAndView.addObject("taskListJSP", listTask);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/edit-task-{id}", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addOrUpdateTask");
        Task editTask = null;


        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            editTask = taskDao.getByPK(id);
            List<User> listUser = userDao.getAll();
            List<Task> listTask = taskDao.getAll();
            modelAndView.addObject("taskJSP", editTask);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(editTask.getDate());
            System.out.println(Utils.calendarToStr(calendar, false));
            modelAndView.addObject("timeTaskJSP", Utils.calendarToStr(calendar, false));
            modelAndView.addObject("tasklistJSP", listTask);
            modelAndView.addObject("userlistJSP", listUser);
            modelAndView.addObject("edit", true);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-task-{id}" }, method = RequestMethod.POST)
    public String updateTask(@ModelAttribute("taskJSP") Task task, @PathVariable Integer id, ModelMap model,
                             HttpServletRequest request) {

        boolean setStatus = setTaskAttribute(request, task);
        if(!setStatus) return "addOrUpdateTask";

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
            Task editTask = taskDao.getByPK(id);

            editTask.setName(task.getName());
            editTask.setDescription(task.getDescription());
            editTask.setContacts(task.getContacts());
            editTask.setDate(task.getDate());
            editTask.setUserId(task.getUserId());
            editTask.setParentId(task.getParentId());
            editTask.setHighPriority(task.isHighPriority());

            taskDao.update(editTask);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "Task " + task.getName() + " updated successfully");
        return "addOrUpdateTaskSuccess";
    }


    /**
     * This method will delete an user by it's SSOID value.
     */
    @RequestMapping(value = { "/delete-task-{id}" }, method = RequestMethod.GET)
    public String deleteTask(@PathVariable Integer id) {

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
            taskDao.delete(taskDao.getByPK(id));
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return "redirect:/taskslist";
    }

    @RequestMapping(value = "/newtask", method = RequestMethod.GET)
    public ModelAndView addTask() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addOrUpdateTask");
        Task task = new Task();
        modelAndView.addObject("taskJSP", task);

        PostgreSqlUserDao userDao = null;
        try {
            userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
            List<User> listUser = userDao.getAll();
            List<Task> listTask = taskDao.getAll();
            modelAndView.addObject("tasklistJSP", listTask);
            modelAndView.addObject("userlistJSP", listUser);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("edit", false);
        return modelAndView;
    }

    @RequestMapping(value = "/newtask", method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("taskJSP") Task task, ModelMap model,
                           HttpServletRequest request) {

        //т.к в taskJSP записывается только простые поля, то приходится
        //вручную обрабатывать сложные теги
        boolean setStatus = setTaskAttribute(request, task);
        if(!setStatus) return "addOrUpdateTask";

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
            taskDao.create(task);
        } catch (PersistException e) {
            e.printStackTrace();
        } catch (NullPointParameterException e) {
            e.printStackTrace();
        } catch (EmptyParamException e) {
            e.printStackTrace();
        }


        model.addAttribute("success", "Task " + task.getName() + " added successfully");
        return "addOrUpdateTaskSuccess";
    }

    private boolean setTaskAttribute(HttpServletRequest request, Task task){
        String value = request.getParameter("time");
        if(value == null) return false;
        Calendar calendar = Utils.strToCalendar(value, false);

        boolean highFlag = false;
        value = request.getParameter("highpriority");
        if(value == null) return false;
        if(value.equals("on")){
            highFlag = true;
        }

        value = request.getParameter("parent");
        String idParentString = null;
        Integer idParent = null;
        if(value != null){
            if(!value.equals("null")){
                idParentString = value.substring( value.indexOf('(')+1, value.length()-1 );
                idParent = Integer.valueOf(idParentString);
            }
        }

        value = request.getParameter("user");
        if(value == null) return false;
        String idUserString = null;
        Integer idUser = null;
        idUserString = value.substring( value.indexOf('(')+1, value.length()-1 );
        idUser = Integer.valueOf(idUserString);

        task.setDate(calendar.getTime());
        task.setHighPriority(highFlag);
        task.setParentId(idParent);
        task.setUserId(idUser);

        return true;
    }
}
