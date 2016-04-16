package taskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import taskManager.dao.*;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import java.sql.Connection;
import java.util.List;


@Controller
public class MainController {


   @Autowired
    private DaoFactory<Connection> factory;

    private Connection connection;

    /*Попадаем сюда на старте приложения  */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userJSP", new User());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /*как только на index.jsp подтвердится форма
    <spring:form method="post"  modelAttribute="userJSP" action="check-user">,
    то попадем вот сюда
     */
    @RequestMapping(value = "/check-user")
    public ModelAndView checkUser(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("mainPage");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        //modelAndView.addObject("userJSP", user);
        //modelAndView.addObject("taskJSP", new Task());

        //получаем соединение, которое будем использовать в др. методах для запросов к БД
        try {
            connection = factory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.GET)
    public ModelAndView addTaskGet()  {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("addOrUpdateTask");
        modelAndView.addObject("taskJSP", new Task());
//        try {
//            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
//            List<Task> list = taskDao.getAll();
//            modelAndView.addObject("parentJSP", list);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, taskManager.domain.User.class);
//            List<taskManager.domain.User> list = userDao.getAll();
//            modelAndView.addObject("usersJSP", list);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        }


        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public ModelAndView addTaskPost(@ModelAttribute("taskJSP") Task task) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        //modelAndView.setViewName("addTask");
        //modelAndView.addObject("taskJSP", new Task());
//        try {
//            PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
//            taskDaoTest.create(task);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        } catch (NullPointParameterException e) {
//            e.printStackTrace();
//        } catch (EmptyParamException e) {
//            e.printStackTrace();
//        }

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/addUser")
    public ModelAndView addUser(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("MainPage");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/removeTask")
    public ModelAndView removeTask(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("addOrUpdateTask");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/removeUser")
    public ModelAndView removeUser(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("addOrUpdateTask");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/userslist", method = RequestMethod.GET)
    public ModelAndView usersList() {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("userslist");
        List<User> list = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            list = userDao.getAll();
            modelAndView.addObject("userListJSP", list);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    /**
     * This method will provide the medium to add a new user.
     */
    @RequestMapping(value = "/newuser" , method = RequestMethod.GET)
    public ModelAndView newUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        User user = new User();
        modelAndView.addObject("userJSP", user);
        modelAndView.addObject("edit", false);
        return modelAndView;
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("userJSP") User user, ModelMap model) {


        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            userDao.create(user);
        } catch (PersistException e) {
            e.printStackTrace();
        } catch (NullPointParameterException e) {
            e.printStackTrace();
        } catch (EmptyParamException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "User " + user.getName() + " registered successfully");
        //return "success";
        return "registrationsuccess";
    }

    /**
     * This method will provide the medium to update an existing user.
     */
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        User editUser = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            editUser = userDao.getByPK(id);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("userJSP", editUser);
        modelAndView.addObject("edit", true);
        return modelAndView;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("userJSP") User user, @PathVariable Integer id, ModelMap model) {

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            User editUser = userDao.getByPK(id);

            editUser.setName(user.getName());
            editUser.setPassword(user.getPassword());

            userDao.update(editUser);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "User " + user.getName() + " updated successfully");
        return "registrationsuccess";
    }


    /**
     * This method will delete an user by it's SSOID value.
     */
    @RequestMapping(value = { "/delete-user-{id}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Integer id) {

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            userDao.delete(userDao.getByPK(id));
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return "redirect:/userslist";
    }

    @RequestMapping(value = "/taskslist", method = RequestMethod.GET)
    public ModelAndView tasksList() {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("taskslist");
        List<User> listUser = null;
        List<Task> listTask = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
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
    public ModelAndView editTask(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addOrUpdateTask");
        Task editTask = null;
        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            editTask = taskDao.getByPK(id);
            List<User> listUser = userDao.getAll();
            List<Task> listTask = taskDao.getAll();
            modelAndView.addObject("taskJSP", editTask);
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
    public String updateUser(@ModelAttribute("taskJSP") Task task, @PathVariable Integer id, ModelMap model) {

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
            Task editTask = taskDao.getByPK(id);

            editTask.setName(task.getName());
            //add  param

            taskDao.update(editTask);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "User " + task.getName() + " updated successfully");
        return "registrationsuccess";
    }


    /**
     * This method will delete an user by it's SSOID value.
     */
    @RequestMapping(value = { "/delete-task-{id}" }, method = RequestMethod.GET)
    public String deleteTask(@PathVariable Integer id) {

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
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
            userDao = (PostgreSqlUserDao) factory.getDao(connection, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
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
    public String saveTask(@ModelAttribute("taskJSP") Task task, ModelMap model) {

        System.out.println(task.isHighPriority());
        System.out.println(task.getParentId());
        System.out.println(task.getUserId());
        System.out.println(task.getDate());
//        try {
//            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
//            taskDao.create(task);
//        } catch (PersistException e) {
//            e.printStackTrace();
//        } catch (NullPointParameterException e) {
//            e.printStackTrace();
//        } catch (EmptyParamException e) {
//            e.printStackTrace();
//        }


        //model.addAttribute("success", "Task " + task.getName() + " added successfully");
        //return "success";
        return "addOrUpdateTask";
    }



}
