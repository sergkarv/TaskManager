package taskManager.controller;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.Taskweb;
import taskManager.domain.User;
import taskManager.postgreSql.*;
import taskManager.utils.Utils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Calendar;

import java.util.List;

@Controller
public class TaskController {

    private PostgreSqlDaoFactory factory;
    private Session session;

    @PostConstruct
    public void postTaskController() {
        factory = new PostgreSqlDaoFactory();
        try {
            session = factory.getContext();
        } catch (PersistException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/taskslist", method = RequestMethod.GET)
    public ModelAndView tasksList() {
        ModelAndView modelAndView = new ModelAndView();

        //name of the view where you want to go
        modelAndView.setViewName("taskslist");
        List<User> listUser = null;
        List<Task> listTask = null;
        try {
            session.flush();
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            listUser = userDao.getAll();
            listTask = taskDao.getAll();
            modelAndView.addObject("userListJSP", listUser);
            modelAndView.addObject("taskListJSP", listTask);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/edit-task-{id}", method = RequestMethod.GET)
    public ModelAndView editTask(@PathVariable Integer id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addOrUpdateTask");
        Task editTask = null;
        Taskweb editTaskWeb = null;

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            editTask = taskDao.getByPK(id);
            editTaskWeb = Utils.taskConvert(editTask);
            List<User> listUser = userDao.getAll();
            List<Task> listTask = taskDao.getAll();
            modelAndView.addObject("taskJSP", editTaskWeb);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(editTask.getDate());
            System.out.println(Utils.calendarToStr(calendar, false));
            modelAndView.addObject("timeTaskJSP", Utils.calendarToStr(calendar, false));
            modelAndView.addObject("tasklistJSP", listTask);
            modelAndView.addObject("userlistJSP", listUser);
            modelAndView.addObject("edit", true);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
        }

        return modelAndView;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating task in database.
     */
    @RequestMapping(value = {"/edit-task-{id}"}, method = RequestMethod.POST)
    public String updateTask(@ModelAttribute("taskJSP") Taskweb taskWeb,
                             @PathVariable Integer id, ModelMap model,
                             HttpServletRequest request) {
        Task task = null;
        String resultPage = null;
        try {
            task = Utils.taskConvert(taskWeb, factory.getDao(session, Task.class), factory.getDao(session, User.class));
            boolean setStatus = setTaskAttribute(request, task);
            if (!setStatus) return "addOrUpdateTask";
        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        }catch (ParseException e){
            resultPage = "errorPage";
            model.addAttribute("error", "Error! Incorrect input date!");
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        }


        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            Task editTask = taskDao.getByPK(id);

            editTask.setName(task.getName());
            editTask.setDescription(task.getDescription());
            editTask.setContacts(task.getContacts());
            editTask.setDate(task.getDate());
            editTask.setUser(task.getUser());
            editTask.setParent(task.getParent());
            editTask.setHighPriority(task.isHighPriority());

            taskDao.update(editTask);
        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        }

        model.addAttribute("success", "Task " + task.getName() + " updated successfully");
        return "addOrUpdateTaskSuccess";
    }


    /**
     * This method will delete an task by it's id value.
     */
    @RequestMapping(value = {"/delete-task-{id}"}, method = RequestMethod.GET)
    public Object deleteTask(@PathVariable Integer id) {
        Object resultPage = null;
        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            taskDao.delete(id);
            resultPage = "redirect:/taskslist";
        } catch (PersistException e) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
            resultPage = modelAndView;
        }

        return resultPage;
    }

    @RequestMapping(value = "/newtask", method = RequestMethod.GET)
    public ModelAndView addTask() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addOrUpdateTask");
        Taskweb taskWeb = new Taskweb();
        modelAndView.addObject("taskJSP", taskWeb);

        PostgreSqlUserDao userDao = null;
        try {
            userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            List<User> listUser = userDao.getAll();
            List<Task> listTask = taskDao.getAll();
            modelAndView.addObject("tasklistJSP", listTask);
            modelAndView.addObject("userlistJSP", listUser);
            modelAndView.addObject("edit", false);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/newtask", method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("taskJSP") Taskweb taskWeb, ModelMap model,
                           HttpServletRequest request) {
        //manual processing of hard tags
        Task task = null;
        String resultPage = null;
        try {
            task = Utils.taskConvert(taskWeb, factory.getDao(session, Task.class), factory.getDao(session, User.class));
            boolean setStatus = setTaskAttribute(request, task);
            if (!setStatus) throw new PersistException("System error!");//return "addOrUpdateTask";
        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        }catch (ParseException e){
            resultPage = "errorPage";
            model.addAttribute("error", "Error! Incorrect input date!");
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        }


        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            taskDao.persist(task, false);
        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        } catch (RuntimeException e) {
            System.err.println(e);
            e.printStackTrace();
            resultPage = "errorPage";
            model.addAttribute("error", "System Error! A data-entry error!");
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
            return resultPage;
        }

        model.addAttribute("success", "Task " + task.getName() + " added successfully");
        return "addOrUpdateTaskSuccess";
    }

    private boolean setTaskAttribute(HttpServletRequest request, Task task) throws PersistException, ParseException {
        String value = request.getParameter("time");
        if (value == null) return false;
        Calendar calendar = Utils.strToCalendar(value, false);

        boolean highFlag = false;
        value = request.getParameter("highpriority");
        if (value == null) {
            highFlag = false;
        } else if (value.equals("on")) {
            highFlag = true;
        }

        value = request.getParameter("parent");
        String idParentString = null;
        Integer idParent = null;
        if (value != null) {
            if (!value.equals("null")) {
                idParentString = value.substring(value.indexOf('(') + 1, value.length() - 1);
                idParent = Integer.valueOf(idParentString);
            }
        }

        value = request.getParameter("user");
        if (value == null) return false;
        String idUserString = null;
        Integer idUser = null;
        idUserString = value.substring(value.indexOf('(') + 1, value.length() - 1);
        idUser = Integer.valueOf(idUserString);

        task.setDate(calendar.getTime());
        task.setHighPriority(highFlag);
        Task parent = (idParent != null) ?
                (Task) factory.getDao(session, Task.class).getByPK(idParent) :
                null;
        task.setParent(parent);
        User user = (User) factory.getDao(session, User.class).getByPK(idUser);
        task.setUser(user);

        return true;
    }
}
