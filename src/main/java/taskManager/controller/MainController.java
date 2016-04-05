package taskManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import taskManager.dao.DaoFactory;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import java.sql.Connection;
import java.util.List;


@Controller
public class MainController {



    private static final DaoFactory<Connection> factory = (DaoFactory<Connection>) new PostgreSqlDaoFactory();
    private static Connection connection;

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
        modelAndView.setViewName("MainPage");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

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
        modelAndView.setViewName("addTask");
        modelAndView.addObject("taskJSP", new Task());
        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(connection, Task.class);
            List<Task> list = taskDao.getAll();
            modelAndView.addObject("parentJSP", list);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(connection, taskManager.domain.User.class);
            List<taskManager.domain.User> list = userDao.getAll();
            modelAndView.addObject("usersJSP", list);
        } catch (PersistException e) {
            e.printStackTrace();
        }


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
        modelAndView.setViewName("addTask");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/removeTask")
    public ModelAndView removeTask(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("addTask");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    @RequestMapping(value = "/removeUser")
    public ModelAndView removeUser(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("addTask");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("userJSP", user);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }


}
