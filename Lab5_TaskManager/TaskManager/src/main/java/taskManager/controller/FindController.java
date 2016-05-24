package taskManager.controller;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Сергей on 22.05.16.
 */
@Controller
public class FindController {

    private PostgreSqlDaoFactory factory;
    private Session session;

    @PostConstruct
    public void postFindController() {
        factory = new PostgreSqlDaoFactory();
        try {
            session = factory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("search");
        return modelAndView;
    }

    @RequestMapping(value = "/search/find_task", method = RequestMethod.GET)
    public ModelAndView findTaskGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("findEqualsTask");

        List<User> listUser = null;
        List<Task> listTask = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            listUser = userDao.getAll();
            listTask = taskDao.getAll();
            modelAndView.addObject("userListJSP", listUser);
            modelAndView.addObject("taskListJSP", listTask);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/search/find_user", method = RequestMethod.GET)
    public ModelAndView findUserGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("findEqualsUser");
        return modelAndView;
    }

    @RequestMapping(value = "/search/find_task", method = RequestMethod.POST)
    public String findTaskPost(HttpServletRequest request, ModelMap model) {
        String result = null;

        Integer id;
        String name;
        String contacts;
        Integer idParent;
        Integer idUser;


        id = (Integer) getParam("Id", request);

        name = (String) getParam("Name", request);

        contacts = (String) getParam("Contacts", request);

        idParent = (Integer) getParam("parent", request);

        idUser = (Integer) getParam("user", request);

        List<Task> listTask = null;
        List<User> listUser = null;
        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            listUser = userDao.getAll();
            listTask = taskDao.getByParameters(id, name, contacts, idParent, idUser);
            Collections.sort(listTask);
            model.addAttribute("taskListJSP", listTask);
            model.addAttribute("userListJSP", listUser);
            result = "taskslist";

        } catch (PersistException e) {
            //e.printStackTrace();
            result = "findError";
            model.addAttribute("error", e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/search/find_user", method = RequestMethod.POST)
    public String findUserPost(HttpServletRequest request, ModelMap model) {
        String result = null;
        Integer id;
        String name;
        String pass;


        id = (Integer) getParam("Id", request);

        name = (String) getParam("Name", request);

        pass = (String) getParam("Password", request);

        List<User> listUser = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);

            listUser = userDao.getByParameters(id, name, pass);
            Collections.sort(listUser);
            model.addAttribute("userListJSP",listUser);
            result = "userslist";
        } catch (PersistException e) {
            //e.printStackTrace();
            result = "findError";
            model.addAttribute("error", e.getMessage());
        }

        return result;
    }

    private Object getParam(String param, HttpServletRequest request){
        Object object = null;
        String value = value = request.getParameter(param);

        switch(param){
            case "Id":
                if(value != null){
                    if(!value.equals("")){
                        object = Integer.valueOf(value);
                    }
                }
                break;
            case "Name":
            case "Password":
            case "Contacts":
                if(value != null){
                    if(!value.equals("")){
                        object = value;
                    }
                }
                break;
            case "user":
                String idUserString = null;
                if(value != null){
                    if(!value.equals("")){
                        idUserString = value.substring( value.indexOf('(')+1, value.length()-1 );
                        object = Integer.valueOf(idUserString);
                    }
                }
                break;
            case "parent":
                String idParentString = null;
                if(value != null){
                    if(!value.equals("null")){
                        idParentString = value.substring( value.indexOf('(')+1, value.length()-1 );
                        object = Integer.valueOf(idParentString);
                    }
                }
                break;
        }

        return  object;
    }

}
